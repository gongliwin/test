package com.gongli;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.gongli.user.mapper")
public class GLUserService {
    public static void main(String[] args) {
        SpringApplication.run(GLUserService.class,args);
    }
}
