package com.gongli.itemdetail.listener;

import com.gongli.itemdetail.service.DetailHtmlService;
import com.gongli.itemdetail.service.GoodsDetailService;
import com.rabbitmq.http.client.domain.ExchangeType;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class GoodsListener {

    @Autowired
    DetailHtmlService detailHtmlService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gongli.create.html.queue",durable = "true"),
            exchange = @Exchange(value = "gongli.item.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void listenCreate(Long id){
        if(id==null){
            return;
        }
        detailHtmlService.createHtml(id);

    }


    public void listenDelete(Long id){
        if(id==null){
            return;
        }
        detailHtmlService.deleteHtml(id);


    }
}
