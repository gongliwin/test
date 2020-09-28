package com.gongli.sms.listener;


import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.gongli.sms.bean.SmsProperties;
import com.gongli.sms.utils.SmsUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.Map;

@ConfigurationProperties(prefix = "gongli.sms")
public class SmsListener {
    @Autowired
    SmsUtils smsUtils;

    @Autowired
    SmsProperties prop;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gongli.sms.queue", durable = "true"),
            exchange = @Exchange(value = "gongli.sms.exchange",
                    ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"sms.verify.code"}))
    public void listenSms(Map<String ,String> msg){
        if (msg == null || msg.size() <= 0) {
            // 放弃处理
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");

//        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
//            // 放弃处理
//            return;
//        }
        // 发送消息
        try {
            SendSmsResponse resp = this.smsUtils.sendSms(phone, code,
                    prop.getSignName(),
                    prop.getVerifyCodeTemplate());
        } catch (ClientException e) {
            throw new RuntimeException();
        }

    }



}
