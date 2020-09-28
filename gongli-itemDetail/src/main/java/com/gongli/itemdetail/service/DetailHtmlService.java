package com.gongli.itemdetail.service;

import com.gongli.itemdetail.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;


@Service
public class DetailHtmlService {

    @Autowired
    GoodsDetailService goodsDetailService;

    @Autowired
    TemplateEngine templateEngine;

    public void createHtml(Long spuId){
        Map<String, Object> map = goodsDetailService.LoadModelData(spuId);

        Context context = new Context();

        File file = new File("D:\\Nginx\\nginx-1.8.0\\html\\item\\"+spuId+".html");

        PrintWriter printWriter=null;
        try {
            printWriter = new PrintWriter(file);
            templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(printWriter!=null){
                printWriter.close();
            }
        }
    }

    public void deleteHtml(Long id) {
        File file = new File("D:\\Nginx\\nginx-1.8.0\\html\\item\\", id + ".html");
        file.deleteOnExit();
    }


    public void asyncExcute(Long spuId){
        ThreadUtils.submit(()->{createHtml(spuId);});
    }

}
