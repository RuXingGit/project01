package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author RuXing
 * @create 2020-04-02 09:57
 */
@Service
public class GoodsService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;

    public Map<String,Object> loadData(Long spuId){
        Map<String,Object> model=new HashMap<>();

        // 查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        // 查询spuDetail
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spuId);
        // 查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        // 各级分类id
        List<Long> ids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        // 根据分类id查询分类名称
        List<String> names = categoryClient.queryNamesByIds(ids);
        // 将分类信息封装为Map对象
        List<Map<String,Object>> categories =new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            Map<String,Object> map=new HashMap<>();
            map.put("id",ids.get(i));
            map.put("name",names.get(i));
            categories.add(map);
        }

        // 查询skus
        List<Sku> skus = goodsClient.querySkuListBySpuId(spuId);

        // 查询规格参数组
        List<SpecGroup> groups = specificationClient.queryGroupWithParamByCid(spu.getCid3());

        // 查询特殊规格参数
        List<SpecParam> params = specificationClient
                                                        // 不是通用的规格参数，特殊规格参数
                .queryParams(null, spu.getCid3(), false, null);
        // 结果封装为Map
        Map<Long,String> paramMap =new HashMap<>();
        params.forEach(param->{
            paramMap.put(param.getId(),param.getName());
        });

        model.put("spu",spu);
        model.put("spuDetail",spuDetail);
        model.put("categories",categories);
        model.put("brand",brand);
        model.put("skus",skus);
        model.put("groups",groups);
        model.put("paramMap",paramMap);

        return model;
    }

}
