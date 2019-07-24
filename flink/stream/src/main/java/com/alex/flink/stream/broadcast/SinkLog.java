package com.alex.flink.stream.broadcast;


import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SinkLog extends RichSinkFunction<Tuple2<String, String>> {
    final  static Logger logger = LoggerFactory.getLogger(SinkLog.class);


    @Override
    public void invoke(Tuple2<String, String> value, Context context) throws Exception {
        logger.info("sink data to log:"+value);
    }




}
