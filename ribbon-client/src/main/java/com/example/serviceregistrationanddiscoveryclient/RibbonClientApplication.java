package com.example.serviceregistrationanddiscoveryclient;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class RibbonClientApplication {

    @LoadBalanced
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public IRule ribbonRule() {
        return new RandomRule();
    }

    public static void main(String[] args) {
        SpringApplication.run(RibbonClientApplication.class, args);
    }
}

@RestController
@Slf4j
class ClientRestController {

    @Autowired
    RestTemplate restTemplate;

    RestTemplate restTemplate2 = new RestTemplate();

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @RequestMapping("/id")
    public Object getID() {
        // @LoadBalanced注解的RestTemplate实例支持自动通过服务名获取具体服务
        // RestTemplate -> InterceptingClientHttpRequest -> LoadBalancerInterceptor -> RibbonLoadBalancerClient -> LoadBalancerRequest.apply
        return this.restTemplate.getForObject("http://producer/id", String.class);
    }

    @RequestMapping("/id2")
    public Object getID2() {
        // 也可以通过LoadBalancerClient先获取服务实例ServiceInstance再请求对应服务地址
        return this.restTemplate2.getForObject(this.loadBalancerClient.choose("producer").getUri() + "/id", String.class);
    }

    @RequestMapping("/timestamp")
    public Object getTimestamp() {
        return this.restTemplate.getForObject("http://producer/timestamp", String.class);
    }

    @RequestMapping("/timestamp2")
    public Object getTimestamp2() {
        return this.restTemplate2.getForObject(this.loadBalancerClient.choose("producer").getUri() + "/timestamp", String.class);
    }
}