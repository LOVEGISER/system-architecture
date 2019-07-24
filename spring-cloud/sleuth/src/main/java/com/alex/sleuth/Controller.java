package com.alex.sleuth;

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
    @RequestMapping(value = "/consume/remote",method = RequestMethod.GET)
    public String service(){
        System.out.println("/consume/remote");
        return restTemplate.getForEntity("http://EUREKA-CLIENT/serviceProducer",String.class).getBody();
    }


}
