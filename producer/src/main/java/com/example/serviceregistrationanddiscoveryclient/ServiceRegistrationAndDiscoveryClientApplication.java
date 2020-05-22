package com.example.serviceregistrationanddiscoveryclient;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

@EnableDiscoveryClient
@SpringBootApplication
public class ServiceRegistrationAndDiscoveryClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistrationAndDiscoveryClientApplication.class, args);
    }
}

@RestController
class ProducerRestController {

    @Autowired
    private Environment environment;

    @RequestMapping("/id")
    public Object generateID() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("port", environment.getProperty("local.server.port"));
        try {
            jsonObject.put("hostName", InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        jsonObject.put("id", new Random().nextLong());
        return jsonObject;
    }

    @RequestMapping("/timestamp")
    public Object generateTimestamp() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("port", environment.getProperty("local.server.port"));
        try {
            jsonObject.put("hostName", InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        jsonObject.put("timestamp", System.currentTimeMillis());
        return jsonObject;
    }
}