package com.gongli.search.api;

import com.gongli.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryApi{

}
