package com.alex.eurekacousumer;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
  * @FeignClient用于通知Feign组件对该接口进行代理(不需要编写接口实现)，使用者可直接通过@Autowired注入。
  * @RequestMapping表示在调用该方法时需要向/serviceProducer 发送GET请求。 *
  */
@FeignClient("EUREKA-CLIENT")
public interface Servers {
    @RequestMapping(value = "/serviceProducer", method = RequestMethod.GET)
    Map serviceProducer();
}
