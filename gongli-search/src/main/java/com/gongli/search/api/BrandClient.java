package com.gongli.search.api;

import com.gongli.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "item-service")
public interface BrandClient extends BrandApi {


}
