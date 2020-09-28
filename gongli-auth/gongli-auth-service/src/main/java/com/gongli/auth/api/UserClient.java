package com.gongli.auth.api;

import com.gongli.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "user-service")
public interface UserClient extends UserApi {
}
