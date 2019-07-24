package com.alex.hystrix;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
public class HystrixController {


    @Autowired
    private RestTemplate restTemplate;

    /**
     * 服务熔断
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "exceptionHandler")
    @RequestMapping(value = "/service/hystrix", method = RequestMethod.GET)
    public String hystrixHandler() {
        return restTemplate.getForEntity("http://EUREKA-CLIENT/serviceProducer", String.class).getBody();
    }

    /**
     * 异步请求
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/service/hystrix/future", method = RequestMethod.GET)
    public String hystrixFeatureHandler() throws ExecutionException, InterruptedException {
        //定义基于Feature的异步调用，请求会以队列形式在线程池中执行
        Future<String> feature = new CommandFeature("feature",restTemplate).queue();
        return feature.get();
    }


    //@RequestMapping(value = "/service/hystrix/callable", method = RequestMethod.GET)
    public String hystrixCallableHandler() throws ExecutionException, InterruptedException {
        List<String> list = new ArrayList<>();
        //定义基于消息订阅的异步调用，请求结果会以事件的方式通知
        Observable<String> observable = new CommandObservable("observer",restTemplate).observe();
      //基于观察者模式的请求结果订阅
        observable.subscribe(new Observer<String>() {
               //所有请求完成后执行
                @Override
                public void onCompleted() {
                    System.out.println("所有请求已经完成...");
                }
                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                }
               //订阅调用事件，请求结果会聚的地方，用集合将返回的结果存放起来。
                @Override
                public void onNext(String s) {
                    System.out.println("结果来了.....");
                    list.add(s);
                }
        });
        return list.toString();
    }

    public String exceptionHandler() {
        return "hystrix ,提供者服务挂了";
    }

}
