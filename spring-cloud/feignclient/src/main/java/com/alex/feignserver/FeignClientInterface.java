package com.alex.feignserver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("EUREKA-CLIENT")
public interface FeignClientInterface {
    @RequestMapping(value = "/serviceProducer", method = RequestMethod.GET)
    Map serviceProducer();
}

