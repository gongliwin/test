package com.gongli.itemdetail.controller;


import com.gongli.itemdetail.service.DetailHtmlService;
import com.gongli.itemdetail.service.GoodsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("item")
public class GoodsDetailController {

    @Autowired
    GoodsDetailService goodsDetailService;

    @Autowired
    DetailHtmlService detailHtmlService;

    @GetMapping("{id}.html")
    String  getGoodsItemHtml(@PathVariable("id") Long id,Model model) {
        Map<String, Object> map = goodsDetailService.LoadModelData(id);

        model.addAllAttributes(map);

        detailHtmlService.asyncExcute(id);
        return "item";
    }

}
