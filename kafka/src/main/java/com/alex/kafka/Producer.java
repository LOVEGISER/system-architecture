package com.alex.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topicName, String message) {
        log.info("send message to kafka {}{}", topicName, message);
        try {
            kafkaTemplate.send(topicName, message);
        } catch (Exception e) {
            log.error("send message error:", e);
        }

        //消息发送的监听器，用于回调返回信息
        kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
            @Override
            public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {
                log.info("message send success,{}{}",topic,value);
            }

            @Override
            public void onError(String topic, Integer partition, String key, String value, Exception exception) {
                log.error("message send error,{}{}",topic,value);
            }


        });
    }

}
