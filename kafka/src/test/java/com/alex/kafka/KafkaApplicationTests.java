package com.alex.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaApplicationTests {
    @Autowired
    private Producer producer;
    @Autowired
    Consumer consumer;
    @Test
    public void contextLoads() {
        try {
            for(int i = 0 ; i<100 ;i++){
                producer.sendMessage("TopicA", "TopicA data:" +i);
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
