package com.leyou.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 接受rabbitmq消息的容器
 *
 * @author RuXing
 * @create 2020-04-03 11:31
 */
@Component
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;


    // 发送验证码
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.SMS.VERIFYCODE", durable = "true"),
            exchange = @Exchange(
                    value = "LEYOU.SMS.EXCHANGE",
                    type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"
            ),
            key = {"verifyCode.sms"}
    ))

    public void sendVerifyCode(Map<String, String> msg) {
        // 参数验证
        if (CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");
        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(code)) {
            try {
                smsUtils.sendSms(phone, code);
            } catch (ClientException e) {
                e.printStackTrace();
            }
            System.out.println("---------------------");
        }

    }
}
