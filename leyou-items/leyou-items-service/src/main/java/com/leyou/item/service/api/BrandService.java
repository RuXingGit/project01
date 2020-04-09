package com.leyou.item.service.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-27 13:42
 */
public interface BrandService {
    PageResult<Brand> queryBrandListByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    void saveBrand(Brand brand, List<Long> cids);

    List<Brand> queryBrandsByCid(Long cid);

    Brand queryBrandById(Long id);
}
