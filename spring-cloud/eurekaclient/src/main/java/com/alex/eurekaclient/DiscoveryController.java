package com.alex.eurekaclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DiscoveryController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Value("${server.port}")
    private String ip;
    @GetMapping("/serviceProducer")
    public Map serviceProducer() {
        //1：服务提供者信息
        String services = "Services:" + discoveryClient.getServices()+" ip :"+ip;
        //2：服务内容
        Map result = new HashMap();
        result.put("serviceProducer",services);
        result.put("time",System.currentTimeMillis());
        return result;
    }
}