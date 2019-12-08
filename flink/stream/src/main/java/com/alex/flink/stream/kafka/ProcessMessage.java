package com.alex.flink.stream.kafka;


import org.apache.directory.api.util.Strings;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */

public class ProcessMessage extends ProcessFunction<String, String> {
    private static final Logger logger = LoggerFactory.getLogger(ProcessMessage.class);


    @Override
    public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
       // logger.info("receive  message:"+value);
        System.out.println("receive  message:"+value);
    }
}
