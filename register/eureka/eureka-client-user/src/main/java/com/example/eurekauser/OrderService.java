package com.example.eurekauser;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description OrderService
 * @Author 吴桂林
 * @Date 2020/1/17 11:55
 * @Version 1.0
 */
@FeignClient("eureka-client-order")
@Service
public interface OrderService {
    @RequestMapping("/")
    public String getHelloWord();

    @RequestMapping("/getDate")
    public long getDate();
}