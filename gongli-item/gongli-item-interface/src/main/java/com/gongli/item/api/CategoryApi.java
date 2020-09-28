package com.gongli.item.api;

import com.gongli.item.controller.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {


    @GetMapping("names")
    List<String> queryCategoryNameByCids(@RequestParam("cids") List<Long> cids);


    @GetMapping("/spec/list/")
    List<Category> queryAllByCids(@RequestParam("cid") Long cid);
}
