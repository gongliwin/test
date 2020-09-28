package com.gongli.itemdetail.api;

import com.gongli.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface SpecClient extends SpecificationApi {
}
