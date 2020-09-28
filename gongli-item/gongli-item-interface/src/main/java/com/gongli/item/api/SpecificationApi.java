package com.gongli.item.api;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("spec")
public interface SpecificationApi {

    @GetMapping("cid/{cid}")
    String findSpecByCid(@PathVariable(value = "cid") Long cid);
}
