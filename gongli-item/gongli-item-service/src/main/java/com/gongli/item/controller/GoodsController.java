package com.gongli.item.controller;


import com.gongli.item.controller.pojo.Sku;
import com.gongli.item.controller.pojo.Spu;
import com.gongli.item.controller.pojo.SpuBo;
import com.gongli.item.controller.pojo.SpuDetail;
import com.gongli.item.service.GoosService;
import com.gongli.page.PageResult;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("goods")
@Api("商品服务接口")
public class GoodsController {

    @Autowired
    GoosService goosService;

    @GetMapping("page")
    @ApiOperation(value = "SpuBo", notes = "分页搜索查询SpuBo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = true, value = "当前页", defaultValue = "1", type = "Integer"),
            @ApiImplicitParam(name = "rows", required = true, value = "每页数", defaultValue = "5", type = "Integer"),
            @ApiImplicitParam(name = "salable", required = false, value = "是否上架", type = "Boolean"),
            @ApiImplicitParam(name = "key", required = false, value = "搜索字段", type = "String"),
    })
    ResponseEntity<PageResult<SpuBo>> querySpuByPageAndFilter(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "salable", required = false) Boolean salable
    ) {

        PageResult<SpuBo> result = goosService.querySpuByPageAndFilter(page, rows, key, salable);

        if (result==null){
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("save")
    @ApiOperation(value = "void",notes = "保存商品")
    @ApiImplicitParam(name = "SpuBo",required = true,value = "SpuBo",type = "SpuBo")
    ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){

        try {
            goosService.save(spuBo);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ApiOperation(notes = "根据spuid查询spu详情",value = "spuDetail")
    @ApiImplicitParam(name = "spuId",required = true,value = "spuId")
    @GetMapping("spu/detail/{spuId}")
    ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable(value = "spuId") Long spuId){
        SpuDetail spuDetail=goosService.querySpuDetailBySpuId(spuId);
        if (spuDetail==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(spuDetail);
    }

    @ApiOperation(notes = "根据spuid查询Sku",value = "Sku集合")
    @ApiImplicitParam(name = "spuId",required = true,value = "spuId")
    @GetMapping("/sku/list")
    ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("spuId") Long spuId){
        List<Sku> lists=goosService.querySkuBySpuId(spuId);
        if (CollectionUtils.isEmpty(lists)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(lists);
    }

    @GetMapping("id")
    ResponseEntity<Spu> querySpuById(@RequestParam("id") Long id){
        Spu spu= goosService.querySpuById(id);
        if(spu==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }




}
