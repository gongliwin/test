package com.gongli.item.controller;


import com.gongli.item.controller.pojo.specification;
import com.gongli.item.service.SpecService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("spec")
@Api("规则服务接口")
public class SpecController {

    @Autowired
    SpecService specService;


    @GetMapping("cid/{cid}")
    @ApiOperation(value = "商品规格json数据",notes = "查询商品规格通过品牌id")
    @ApiImplicitParam(name = "种类id",required = true,value = "cid",type = "Long")
    ResponseEntity<String> findSpecByCid(@PathVariable(value = "cid") Long cid){
         specification specification= specService.findSpecByBid(cid);

         if(specification==null){
             return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
         }

        return ResponseEntity.ok(specification.getSpecifications());
    }

}
