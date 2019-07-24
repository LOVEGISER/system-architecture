package com.alex.feignserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FeignController {
    @Autowired
    FeignClientInterface feignClientInterface;
    @RequestMapping(value = "/consume/feign",method = RequestMethod.GET)
    public Map serviceFeign(){
        return feignClientInterface.serviceProducer();
    }
}
