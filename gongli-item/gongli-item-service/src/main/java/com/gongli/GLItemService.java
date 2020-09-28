package com.gongli;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@tk.mybatis.spring.annotation.MapperScan("com.gongli.item.mapper")
public class GLItemService {

    public static void main(String[] args) {
        SpringApplication.run(GLItemService.class,args);
    }
}
