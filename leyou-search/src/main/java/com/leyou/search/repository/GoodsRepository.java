package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 调用elasticsearch进行CRUD的接口
 * @author RuXing
 * @create 2020-03-31 17:05
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
