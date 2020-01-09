package com.thtf.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ---------------------------
 * 网关启动类
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/2 14:35
 * 版本：  v1.0
 * ---------------------------
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
