package com.elite.tetto.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.elite.tetto.auth.dao")
public class Auth {
    public static void main(String[] args) {
        SpringApplication.run(Auth.class, args);
    }
}
