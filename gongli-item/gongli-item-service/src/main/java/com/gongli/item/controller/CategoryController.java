package com.gongli.item.controller;

import com.gongli.enums.ExceptionEnum;
import com.gongli.exception.myException;
import com.gongli.item.controller.pojo.Category;
import com.gongli.item.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("category")
@Api("种类服务接口")
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @GetMapping("list")
    @ApiOperation(value = "商品种类集合", notes = "查询商品种类列表通过商品父id")
    @ApiImplicitParam(name = "父id", required = true, value = "pid", defaultValue = "0", type = "Long")
    public ResponseEntity<List<Category>> queryCategoryListByParentId(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        if (pid == null || pid.longValue() < 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            List<Category> list = categoryService.queryCategoryListByParentId(pid);

            if (CollectionUtils.isEmpty(list)) {

                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }


    @ApiOperation(value = "商品3名称", notes = "查询商品名称列表通过那3个id")
    @ApiImplicitParam(name = "3id名字集合", required = true, value = "cids", type = "List<Long>")
    @GetMapping("names")
    ResponseEntity<List<String>> queryCategoryNameByCids(@RequestParam("cids") List<Long> cids) {

        List<String> lists = categoryService.queryCategoryNameByCids(cids);

        if (CollectionUtils.isEmpty(lists)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lists);
    }

    @ApiOperation(value = "商品集合", notes = "查询商品集合通过id")
    @ApiImplicitParam(name = "3id种类集合", required = true, value = "cid", type = "Long")
    @GetMapping("/spec/list/")
    ResponseEntity<List<Category>> queryAllByCids(@RequestParam("cid") Long cid) {

        List<Category> lists = categoryService.queryAllByCid3(cid);

        if (CollectionUtils.isEmpty(lists)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lists);
    }


}
