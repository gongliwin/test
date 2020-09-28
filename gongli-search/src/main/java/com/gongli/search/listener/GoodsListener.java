package com.gongli.search.listener;


import com.gongli.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class GoodsListener {

    @Autowired
    SearchService searchService;


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "gongli.create.index.queue",durable = "true"),
    exchange = @Exchange(value = "gongli.item.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void listenCreate(Long id) throws Exception{
        if(id==null){
            return;
        }
        searchService.createIndex(id);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gongli.delete.index.queue",durable = "true"),
            exchange = @Exchange(value = "gongli.item.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void listenDelete(Long id)throws Exception{
        if(id==null){
            return;
        }
        searchService.createDelete(id);
    }
}
