package com.leyou.search.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 *
 * 用来接受发送验证码的消息，假装发送验证码了
 * @author RuXing
 * @create 2020-04-03 12:33
 */
@Component
public class UserListener {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.sms.queue",durable = "true"),
            exchange = @Exchange(
                    value = "LEYOU.SMS.EXCHANGE",
                    type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions="true"
            ),
            key = {"verifyCode.sms"}
    ))
    public void send(Map<String,String> msg){
        System.out.println("验证码已经发送到控制台了（滑稽）\n"
                +msg.get("phone")+":"+msg.get("code"));
    }
}
