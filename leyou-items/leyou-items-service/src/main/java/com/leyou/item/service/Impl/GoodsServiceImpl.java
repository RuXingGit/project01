package com.leyou.item.service.Impl;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectRestriction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.item.service.api.CategoryService;
import com.leyou.item.service.api.GoodsService;
import com.leyou.item.vo.SpuVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author RuXing
 * @create 2020-03-28 17:37
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    // 获取页面的spu信息，将spu数据在页面显示
    @Override
    @Transactional(readOnly = true)
    public PageResult<SpuVO> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        // 添加查询条件key和saleable
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        // 添加分页
        PageHelper.startPage(page, rows);
        // 查询商品集
        List<Spu> spus = spuMapper.selectByExample(example);

        // 类型转化为spuVO
        List<SpuVO> spuVOS = spus.stream().map(spu -> {
            SpuVO spuVO = new SpuVO();
            BeanUtils.copyProperties(spu, spuVO);
            // 查询品牌名称
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuVO.setBname(brand.getName());
            // 查询分类名称
            List<String> categoryNames = categoryService.queryCategoryNameByIds(
                    Arrays.asList(
                            spu.getCid1(), spu.getCid2(), spu.getCid3()
                    ));
            // 将分类名称赋值给显示视图的对象
            spuVO.setCname(StringUtils.join(categoryNames, "-"));

            // 返回一个SpuVO对象
            return spuVO;
        }).collect(Collectors.toList());
        // 封装成pageInfo
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        // 返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), spuVOS);
    }

    // 将商品信息保存到数据库
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveGoods(SpuVO spuVO) {
        // 设置默认值
        spuVO.setId(null);
        spuVO.setSaleable(true);
        spuVO.setValid(true);
        spuVO.setCreateTime(new Date());
        spuVO.setLastUpdateTime(spuVO.getCreateTime());

        // 数据库表新增的顺序
        // spu
        spuMapper.insertSelective(spuVO);
        // spu detail
        SpuDetail spuDetail = spuVO.getSpuDetail();
        // 这里的id不再是null，而是插入后返回的自增主键
        spuDetail.setSpuId(spuVO.getId());
        spuDetailMapper.insertSelective(spuDetail);

        // 保存sku和stock
        saveSkuAndStock(spuVO);

        // 新增了数据需要进行同步
        sendMsg("item.insert",spuVO.getId());

    }

    // 发送消息到rabbitmq
    private void sendMsg(String routingKey,Object message){
        try {
            amqpTemplate.convertAndSend(routingKey,message);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    // 更新spu信息 需要更新的表有spu、spu detail、sku、stock
    @Override
    @Transactional
    public void updateGoods(SpuVO spuVO) {
        // 更新方式：先删除、后插入新的信息

        // 根据spuId查找sku
        Sku record = new Sku();
        record.setSpuId(spuVO.getId());
        List<Sku> skuList = skuMapper.select(record);
        skuList.forEach(sku -> {
            // 根据sku删除stock表中的记录
            stockMapper.deleteByPrimaryKey(sku.getId());
        });
        // 根据spuId删除sku
        Sku sku=new Sku();
        sku.setSpuId(spuVO.getId());
        skuMapper.delete(sku);


        // 新增sku和stock
        saveSkuAndStock(spuVO);

        // 更新spu和spuDetail

        // 不可以修改的属性
        spuVO.setCreateTime(null);
        spuVO.setLastUpdateTime(new Date());
        spuVO.setSaleable(null);
        spuVO.setValid(null);
        spuMapper.updateByPrimaryKeySelective(spuVO);
        spuDetailMapper.updateByPrimaryKeySelective(spuVO.getSpuDetail());

        // 向rabbitmq发送消息
        sendMsg("item.update",spuVO.getId());
    }

    @Override
    public Spu querySpuById(Long id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    @Override
    public Sku querySkuById(Long skuId) {
        return skuMapper.selectByPrimaryKey(skuId);
    }

    private void saveSkuAndStock(SpuVO spuVO){
        // sku & stock
        spuVO.getSkus().forEach(sku -> {

            sku.setCreateTime(new Date());
            sku.setSpuId(spuVO.getId());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insertSelective(sku);


            // stock对象保存
            Stock stock=new Stock();
            // skuId是插入后返回的自增主键
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);


        });
    }

    // 根据id查询spu详情
    @Override
    @Transactional(readOnly = true)
    public SpuDetail querySpuDetailById(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    // 根据id查询sku信息
    @Override
    @Transactional(readOnly = true)
    public List<Sku> querySkuListBySpuId(Long spuId) {
        Sku record=new Sku();
        record.setSpuId(spuId);

        List<Sku> skuList = skuMapper.select(record);
        skuList.forEach(sku -> {
            Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });

        return skuList;
    }


}
