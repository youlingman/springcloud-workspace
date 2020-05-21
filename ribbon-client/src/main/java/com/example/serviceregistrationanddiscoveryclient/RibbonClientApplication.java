package com.example.serviceregistrationanddiscoveryclient;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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
class ClientRestController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    DiscoveryClient discoveryClient;

    @RequestMapping("/id")
    public Object getID() {
        this.loadBalancerClient.choose("producer");
        return this.restTemplate.getForObject("http://producer/id", String.class);
    }

    @RequestMapping("/timestamp")
    public Object getTimestamp() {
        this.loadBalancerClient.choose("producer");
        return this.restTemplate.getForObject("http://producer/timestamp", String.class);
    }
}