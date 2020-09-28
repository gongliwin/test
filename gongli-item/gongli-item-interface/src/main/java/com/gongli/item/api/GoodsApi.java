package com.gongli.item.api;


import com.gongli.item.controller.pojo.Sku;
import com.gongli.item.controller.pojo.Spu;
import com.gongli.item.controller.pojo.SpuBo;
import com.gongli.item.controller.pojo.SpuDetail;
import com.gongli.page.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("goods")
public interface GoodsApi {

    @GetMapping("page")
    PageResult<SpuBo> querySpuByPageAndFilter(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "salable", required = false) Boolean salable
    );

    @GetMapping("spu/detail/{spuId}")
    SpuDetail querySpuDetailBySpuId(@PathVariable(value = "spuId") Long spuId);


    @GetMapping("/sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("spuId") Long spuId);

    @GetMapping("id")
    Spu querySpuById(@RequestParam("id") Long id);

}
