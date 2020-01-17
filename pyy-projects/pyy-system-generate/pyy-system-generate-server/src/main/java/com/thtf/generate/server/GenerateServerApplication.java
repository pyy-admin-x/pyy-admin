package com.thtf.generate.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * ---------------------------
 * 代码生成启动类
 * ---------------------------
 * 作者：  pyy
 * 时间：  2019/12/30 10:37
 * 版本：  v1.0
 * ---------------------------
 */
@MapperScan("com.thtf.generate.server.mapper")
@ComponentScan(basePackages = "com.thtf")
@EnableDiscoveryClient
@SpringBootApplication
public class GenerateServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenerateServerApplication.class, args);
    }
}
