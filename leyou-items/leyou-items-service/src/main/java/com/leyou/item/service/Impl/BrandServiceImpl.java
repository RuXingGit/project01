package com.leyou.item.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.api.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-27 13:42
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;


    // 根据关键字查询品牌信息并分页
    @Override
    @Transactional(readOnly = true)
    public PageResult<Brand> queryBrandListByPage(String key,
                                                  Integer page,
                                                  Integer rows,
                                                  String sortBy,
                                                  Boolean desc) {
        // 根据name模糊查询，或根据首字母查询
        // 添加分页
        // 添加排序条件

        // 创建example对象
        Example example = new Example(Brand.class);

        // 对数据进行判断，在key有值的时候才进行模糊查询
        if (StringUtils.isNotBlank(key)) {
            Example.Criteria criteria = example.createCriteria();
            // 根据名字模糊查询或首字母查询
            criteria.andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key);
        }
        // 添加分页条件
        PageHelper.startPage(page, rows);
        // 排序
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        // 查询品牌
        List<Brand> brands = brandMapper.selectByExample(example);

        // 包装结果
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);

        //返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }

    // 新增品牌
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public void saveBrand(Brand brand, List<Long> cids) {
        // 新增brand
        brandMapper.insertSelective(brand);
        // 在中间表添加品牌和分类关系
        cids.forEach(cid -> {
            this.brandMapper.insertCategoryAndBrand(cid, brand.getId());
        });

    }

    // 根据分类表中的三级id查询品牌
    @Override
    @Transactional(readOnly = true)
    public List<Brand> queryBrandsByCid(Long cid) {


        return brandMapper.selectBrandsByCid(cid);
    }

    // 根据id查询品牌
    @Override
    @Transactional(readOnly = true)
    public Brand queryBrandById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }
}
