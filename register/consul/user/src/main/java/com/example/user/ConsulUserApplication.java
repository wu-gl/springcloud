package com.example.user;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/test")
@EnableDiscoveryClient
@EnableConfigurationProperties({UserConfig.class})      //读取consul中的配置信息，读取到UserConfig
@EnableTurbine          //Turbine集群监控
@EnableFeignClients     //启动Feigen
@EnableHystrix          //启用Hystrix服务降级和熔断
@EnableHystrixDashboard //启用Hystrix看板
public class ConsulUserApplication {


    /**
     * 获取config/application/data下myName的数据
     * 获取config/user/data下myName的数据
     * 获取config/user,dev/data下myName的数据
     * <p>
     * 越往下优先级越高，会覆盖上层的数据
     */
    @Value("${myName}")
    private String myName;

    @RequestMapping("/myname")
    public String testHello() {
        System.out.println("my name is : " + myName);
        return myName;
    }


    /**
     * 获取config/application/data下myName的数据
     * 获取config/user/data下myName的数据
     * 获取config/user,dev/data下myName的数据
     * <p>
     * 越往下优先级越高，会覆盖上层的数据
     */
    @Autowired
    private UserConfig userConfig;

    @RequestMapping("/config")
    public String testConfig() {
        System.out.println(userConfig.toString());
        return userConfig.getName();
    }

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
        return this.restTemplate.getForObject("http://order/", String.class);
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
        List<ServiceInstance> list = discoveryClient.getInstances("order");
        if (list != null && list.size() > 0) {
            return list.get(0).getUri().toString();
        }
        return null;
    }

    /**
     * 还可以通过Feign来服务间调用
     */
    @FeignClient("order")
    static interface OrderService {
        @RequestMapping("/")
        public String getHelloWord();

        @RequestMapping("/getDate")
        public long getDate();
    }


    @Autowired
    OrderService orderService;

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

    /**
     * 定义hystrix.stream的配置信息
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean<HystrixMetricsStreamServlet> getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean = new ServletRegistrationBean<HystrixMetricsStreamServlet>(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("用户服务");
        return registrationBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsulUserApplication.class, args);
    }
}
