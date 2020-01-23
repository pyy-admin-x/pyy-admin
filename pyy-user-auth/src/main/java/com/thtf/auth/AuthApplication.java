package com.thtf.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * ---------------------------
 * 认证（授权）服务启动类
 * ---------------------------
 * 作者：  pyy
 * 时间：  2020/1/20 15:11
 * 版本：  v1.0
 * ---------------------------
 */
@ComponentScan(basePackages = "com.thtf")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.thtf.auth.feign"})
@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
