package com.elite.tetto.third_party;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ThirdParty {
    public static void main(String[] args) {
        SpringApplication.run(ThirdParty.class, args);
    }
}
