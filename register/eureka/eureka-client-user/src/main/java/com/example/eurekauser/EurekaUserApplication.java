package com.example.eurekauser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/test")
@EnableFeignClients
public class EurekaUserApplication {

    @LoadBalanced
    @Bean
    /**
     * 用于初始化RestTemplate
     */
    public RestTemplate loadbalancedRestTemplate() {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    /**
     * 通过RestTemplate请求order服务
     *
     * @return
     */
    public String getFirstProduct() {
        return this.restTemplate.getForObject("http://eureka-client-order/", String.class);
    }

    /**
     * 验证RestTemplate
     *
     * @return
     */
    @RequestMapping("/getOrderInfo")
    public String getOrderInfo() {
        return getFirstProduct();
    }


    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/getOrderInfo1")
    /**
     *可以通过该方法获取order的所有服务
     */
    public String getOrderInfo1() {
        List<ServiceInstance> list = discoveryClient.getInstances("eureka-client-order");
        if (list != null && list.size() > 0) {
            return list.get(0).getUri().toString();
        }
        return null;
    }
    /**
     * 还可以通过Feign来服务间调用
     */
    @Autowired(required = true)
    private OrderService orderService;

    /**
     * 验证Feign
     *
     * @return
     */
    @RequestMapping("/getHelloWord")
    public String getHelloWord() {
        return orderService.getHelloWord();
    }

    @RequestMapping("/getDate")
    public long getDate() {
        return orderService.getDate();
    }

    /**
     * 用于健康检查，当然可以加入各种权限验证，别人任何人都可以请求
     *
     * @return
     */
    @RequestMapping("/")
    public String home() {
        return "Hello world";
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaUserApplication.class, args);
    }
}
