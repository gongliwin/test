package com.gongli.item.controller;


import com.gongli.item.controller.pojo.Brand;
import com.gongli.item.controller.pojo.Category;
import com.gongli.item.service.BrandService;
import com.gongli.page.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
@Api("品牌服务接口")
public class BrandController {

    @Autowired
    BrandService brandService;

    @GetMapping("page")
    @ApiOperation(value = "品牌", notes = "分页搜索排序查询品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = true, value = "当前页", defaultValue = "1", type = "Integer"),
            @ApiImplicitParam(name = "rows", required = true, value = "每页数", defaultValue = "5", type = "Integer"),
            @ApiImplicitParam(name = "sortBy", required = false, value = "排序", type = "String"),
            @ApiImplicitParam(name = "desc", required = true, value = "顺还是逆", defaultValue = "false", type = "Boolean"),
            @ApiImplicitParam(name = "key", required = false, value = "搜索字段", type = "String"),
    })
    ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key

    ) {

        PageResult<Brand> pageResult = brandService.queryBrandByPage(page, rows, sortBy, desc, key);

        if (pageResult == null || pageResult.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //return ResponseEntity.ok(pageResult);
        return ResponseEntity.status(HttpStatus.OK).body(pageResult);
    }


    @PostMapping
    @ApiOperation(value = "void", notes = "根据种类id添加品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cids", required = true, value = "3级种类id",type = "List<Long>"),
            @ApiImplicitParam(name = "brand", required = true, value = "品牌类")
    })
    ResponseEntity<Void> addBrandBycids(Brand brand, @RequestParam(value = "cids") List<Long> cids) {

        System.out.println("龚力" + brand.getId());
        brandService.insertBrandBycids(brand, cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("bid/{bid}")
    @ApiOperation(value = "商品种类集合", notes = "根据品牌id查找商品种类")
    @ApiImplicitParam(name = "bid", required = true, value = "品牌id",type = "Long")
    ResponseEntity<List<Category>> findCategoryByBid(@PathVariable("bid") Long bid) {
        List<Category> category = brandService.findCategoryByBid(bid);
        if (category == null || category.size() < 1) {
            return new ResponseEntity<List<Category>>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(category);
    }

    @GetMapping("cid/{cid}")
    @ApiOperation(value = "商品种类集合", notes = "根据种类id查找商品种类")
    @ApiImplicitParam(name = "cid", required = true, value = "种类id",type = "Long")
    ResponseEntity<List<Brand>> findBrandBycid(@PathVariable("cid") Long cid) {
        List<Brand> category = brandService.findBrandByCid(cid);
        if (category == null || category.size() < 1) {
            return new ResponseEntity<List<Brand>>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(category);
    }

    @GetMapping("/brand/bid/{bid}")
    @ApiOperation(value = "商品品牌", notes = "根据商品id查找商品品牌")
    @ApiImplicitParam(name = "bid", required = true, value = "品牌id",type = "Long")
    ResponseEntity<Brand> findBrandByBid(@PathVariable("bid") Long bid) {
        Brand category = brandService.findBrandByBid(bid);
        if (category == null ) {
            return new ResponseEntity<Brand>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(category);
    }

    @GetMapping("brands")
    @ApiOperation(value = "商品集合", notes = "根据品牌id集合查找商品集合")
    @ApiImplicitParam(name = "bids", required = true, value = "品牌id集合",type = "List<Long>")
    ResponseEntity<List<Brand>> findBrandsByBids(@RequestParam("bid") List<Long> bids) {
        List<Brand> brands = brandService.findBrandsByBids(bids);
        if (brands == null ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(brands);
    }


}
