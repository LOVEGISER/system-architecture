package com.alex.flink.stream.kafka;


import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class YMKafkaSource extends RichSourceFunction< String>{
    final  static Logger logger = LoggerFactory.getLogger(YMKafkaSource.class);
    private volatile boolean isRunning = true;


    private  KafkaConsumer<String, String> consumer;

    private  String topic;
    private static final String GROUPID = "CG_YEAHDSP_FLINK_save_event";
    Properties props ;
    public YMKafkaSource(String topicName) {
        this.topic = topicName;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        props = new Properties();
        props.put("bootstrap.servers", "10.8.5.13:9092,10.8.5.14:9092,10.8.105.13:9092");
        props.put("group.id", GROUPID);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topic));
        logger.info("*********************open****************************");
    }

    @Override
    public void run(SourceContext<String> ctx ) throws Exception {
        logger.info("*********************while****************************");
        while (isRunning) {
            try {
                logger.info("********************* before poll****************************");
                ConsumerRecords<String, String> msgList = consumer.poll(1000);
                logger.info("*********************after poll****************************"+msgList.count());
                if (null != msgList && msgList.count() > 0) {
                    for (ConsumerRecord<String, String> record : msgList) {
                        ctx.collect(record.value());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void cancel() {
        isRunning = false;
            consumer.close();
    }
}
