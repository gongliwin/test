package com.gongli.item.api;

import com.gongli.item.controller.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("brand")
public interface BrandApi {

    @GetMapping("brands")
    List<Brand> findBrandsByBids(@RequestParam("bid") List<Long> bids);
}
