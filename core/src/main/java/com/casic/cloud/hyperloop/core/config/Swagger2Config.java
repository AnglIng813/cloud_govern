package com.casic.cloud.hyperloop.core.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${apis.title}")
    private String title;
    @Value("${apis.description}")
    private String description;
    @Value("${apis.version}")
    private String version;
    @Value("${apis.packageName}")
    private String packageName;

    @Bean
    public Docket createTest1RestApi() {
        //添加header参数
        ParameterBuilder builder = new ParameterBuilder();
        List<Parameter> paramList = new ArrayList<>();
        builder.name("Authorization").description("token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false); //header中的Authorization参数非必填
        paramList.add(builder.build());//根据每个方法名也知道当前方法在设置什么参数

        Docket d = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(packageName))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(paramList);
        return d;
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .version(version)
                .description(description)
                .build();
    }



}
