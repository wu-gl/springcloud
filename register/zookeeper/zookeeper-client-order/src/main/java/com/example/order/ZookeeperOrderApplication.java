package com.example.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class ZookeeperOrderApplication {

    @RequestMapping("/")
    public String home() {
        return "Hello world";
    }

    @RequestMapping("/getDate")
    public long getDate() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SpringApplication.run(ZookeeperOrderApplication.class, args);
    }
}
