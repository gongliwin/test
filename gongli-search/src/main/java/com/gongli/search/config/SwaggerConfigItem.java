package com.gongli.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfigItem {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).host("").apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.gongli.search.controller")).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("gongliwin商城").description("gongliwin商城").version("1.0.0").build();
    }
}
