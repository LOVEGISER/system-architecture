package com.alex.hbase;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Map;

@Component
public class Scheduler{
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private Logger log = LoggerFactory.getLogger(Scheduler.class);
    @Autowired
    private HBaseService hbaseService;
//    String tableNanme = "t1";
//    String cf = "f1";
    String tableNanme = "student";
    String cf = "info";
    //每隔2秒执行一次
    //create 'emp', 'personal data', ’professional data’
    @Async()//异步线程消费，提供并发度
    @Scheduled(fixedRate = 2000)
    public void hbaseInertTasks() {
        //System.out.println("定时任务执行时间：" + dateFormat.format(new Date()));
        long start = System.currentTimeMillis();
        Map<String, String> result = hbaseService.get(tableNanme, cf, "01");
        long end = System.currentTimeMillis();
        log.info("time:["+(end-start)+"]"+ JSON.toJSONString(result));
    }



}
