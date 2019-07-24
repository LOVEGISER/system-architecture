package com.alex.flink.stream.broadcast;


import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSource extends RichSourceFunction<String> {
    final  static Logger logger = LoggerFactory.getLogger("jobA_file");
    private volatile boolean isRunning = true;


    @Override
    public void open(Configuration parameters) throws Exception {

    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        while(isRunning){
            ctx.collect("1");
            logger.info("send message:{}:"+System.currentTimeMillis());
            Thread.sleep(1000l);
        }
    }



    @Override
    public void cancel() {
        isRunning = false;
    }
}
