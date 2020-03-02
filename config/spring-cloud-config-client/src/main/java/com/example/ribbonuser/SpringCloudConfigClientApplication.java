package com.example.ribbonuser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/test")
@EnableEurekaClient
@RefreshScope
class SpringCloudConfigClientApplication {

    @Value("${data.user.username}")
    private String userName;

    @RequestMapping("/getUserName")
    public String getUserName() {
        System.out.println("my name is : " + userName);
        return userName;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigClientApplication.class, args);
    }
}
