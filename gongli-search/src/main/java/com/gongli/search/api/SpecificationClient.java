package com.gongli.search.api;

import com.gongli.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "item-service")
public interface SpecificationClient extends SpecificationApi {
}
