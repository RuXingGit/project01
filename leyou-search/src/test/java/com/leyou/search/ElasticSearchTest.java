package com.leyou.search;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.vo.SpuVO;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author RuXing
 * @create 2020-03-31 17:02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticSearchTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsClient goodsClient;


    @Test
    public void test(){
        // 创建索引和映射
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
        // 从数据库导入参数到elasticsearch

        Integer page =1;
        Integer rows =100;

        do{
            PageResult<SpuVO> result = goodsClient.querySpuByPage(null, null, page, rows);
            List<SpuVO> items = result.getItems();
            List<Goods> goodsList = items.stream().map(spuVO -> {
                try {
                    return searchService.buildGoods(spuVO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());

            goodsRepository.saveAll(goodsList);

            rows=items.size();
            page++;
            System.out.println("--------------"+page);
        }while (rows==100);



    }
}
