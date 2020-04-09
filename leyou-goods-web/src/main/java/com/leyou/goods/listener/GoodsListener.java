package com.leyou.goods.listener;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author RuXing
 * @create 2020-04-02 17:57
 */
@Component
public class GoodsListener {
    @Autowired
    private GoodsHtmlService goodsHtmlService;
    @Autowired
    private GoodsService goodsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.ITEM.INSERT.QUEUE",durable = "true"),
            exchange = @Exchange(
                    value = "LEYOU.ITEM.EXCHANGE",
                    type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"
            ),
            key = {"item.insert","item.update"}
    ))
    public void saveListener(Long id){
        if(id==null){
            return;
        }
        // 更新静态页面
        Map<String, Object> data = goodsService.loadData(id);
        goodsHtmlService.createHtml(id,data);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.ITEM.DELETE.QUEUE",durable = "true"),
            exchange = @Exchange(
                    value = "LEYOU.ITEM.EXCHANGE",
                    type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"
            ),
            key = {"item.delete"}
    ))
    public void deleteListener(Long id){
        if(id==null){
            return;
        }
        // 更新静态页面
        Map<String, Object> data = goodsService.loadData(id);
        goodsHtmlService.deleteHtml(id);
    }

}
