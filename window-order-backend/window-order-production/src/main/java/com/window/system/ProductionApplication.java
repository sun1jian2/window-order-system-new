package com.window.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@MapperScan("com.window.system.mapper")
@EnableDiscoveryClient
@EnableFeignClients
// 排除通用模块中不需要在生产模块初始化的组件（例如依赖 minio 的 SysExportTaskService 等）
@ComponentScan(
    basePackages = {"com.window.system"},
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.window.system.service.SysExportTaskService"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.window.system.config.MinioConfig")
    }
)
public class ProductionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductionApplication.class, args);
    }
}
