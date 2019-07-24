package com.alex.hystrix;


import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.springframework.web.client.RestTemplate;

public class CommandFeature extends HystrixCommand<String> {
    private String name;
    private RestTemplate restTemplate;
    public CommandFeature(String name, RestTemplate restTemplate) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                /* HystrixCommandKey工厂定义依赖名称 */
                .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld"))
                /* 使用HystrixThreadPoolKey工厂定义线程池名称*/
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HelloWorldPool")));

        this.name = name;
        this.restTemplate = restTemplate;
    }
    //远程调用方法体
    @Override
    protected String run() {
        String result = restTemplate.getForEntity("http://EUREKA-CLIENT/serviceProducer", String.class).getBody();
        return result;
    }
   //服务降级处理逻辑
    @Override
    protected String getFallback() {
        return "hystrix:远程服务异常";
    }
}
