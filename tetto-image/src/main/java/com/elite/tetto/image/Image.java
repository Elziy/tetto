package com.elite.tetto.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Image {
    public static void main(String[] args) {
        SpringApplication.run(Image.class, args);
    }
}