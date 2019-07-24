package com.alex.eurekacousumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class Controller {
    @Autowired
    private RestTemplate restTemplate;

  @Autowired Servers server;

    @RequestMapping(value = "/consume/remote",method = RequestMethod.GET)
    public String service(){
        return restTemplate.getForEntity("http://EUREKA-CLIENT/serviceProducer",String.class).getBody();
    }

    @RequestMapping(value = "/consume/feign",method = RequestMethod.GET)
    public Map serviceFeign(){
        return server.serviceProducer();
    }


}
