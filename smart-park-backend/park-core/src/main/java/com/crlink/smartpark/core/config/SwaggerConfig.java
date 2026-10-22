package com.crlink.smartpark.core.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalTime;

/**
 * @ClassName SwaggerConfig
 * @description: SwaggerConfig
 **/
@Configuration
@EnableSwagger2 // 启用 Swagger
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .directModelSubstitute(LocalTime.class, String.class)
                .apiInfo(apiInfo())
                .select()
                //这里采用包含注解的方式来确定要显示的接口(建议使用这种)
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建 api 文档的详细信息函数，注意这里的注解引用的是哪个
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("智慧园区在线接口文档")
                // 创建人
                .contact(new Contact("park", "http://www.park.crc.com.cn", "xxx@crc.com.hk"))
                // 版本号
                .version("1.0")
                // 描述
                .description("智慧园区在线接口文档")
                .build();
    }
}

