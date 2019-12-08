package com.alex.flink.stream.kafka;


import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title: DspAerospikeSink
 * @Package: com.yeahmobi.dsp.sink
 * @Description: TODO
 * @author: Mo.Lee
 * @date: 2019/10/16
 * @since: 1.0
 */
public class KafkaSink extends RichSinkFunction<String> {
    private static final Logger logger = LoggerFactory.getLogger(KafkaSink.class);


    @Override
    public void open(Configuration parameters) throws Exception {

    }

    @Override
    public void invoke(String value, Context context) throws Exception {
       // logger.info("IFA:{}, count:{}");
        System.out.println("sink  message:"+value);
    }


}
