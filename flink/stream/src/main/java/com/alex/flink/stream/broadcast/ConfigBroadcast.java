package com.alex.flink.stream.broadcast;

import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigBroadcast extends BroadcastProcessFunction<String,Tuple2<String,String>,Tuple2<String, String>> {
    private static final Logger logger = LoggerFactory.getLogger("jobB_file");

    private MapStateDescriptor<String, String> apolloConfigDescriptor;

    private final String key = "click";
    public ConfigBroadcast(MapStateDescriptor<String, String> apolloConfigDescriptor) {
        this.apolloConfigDescriptor = apolloConfigDescriptor;
    }

    @Override
    public void processElement(String value, ReadOnlyContext ctx, Collector<Tuple2<String, String>> out) throws Exception {
        ReadOnlyBroadcastState<String,String> config = ctx.getBroadcastState(apolloConfigDescriptor);
        logger.info("click:{}",config.get(key));
    }

    @Override
    public void processBroadcastElement(Tuple2<String, String> value, Context ctx, Collector<Tuple2<String, String>> out) throws Exception {
        logger.info("config change {}:{}",value.f0,value.f1);
        ctx.getBroadcastState(apolloConfigDescriptor).put(value.getField(0),value.getField(1));

    }
}
