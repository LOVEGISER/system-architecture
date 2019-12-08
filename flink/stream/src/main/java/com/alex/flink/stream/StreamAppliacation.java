package com.alex.flink.stream;

import com.alex.flink.stream.ymclick.WordCount;
import com.alibaba.fastjson.JSON;
import netscape.javascript.JSObject;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Properties;

public class StreamAppliacation {
    final  static  Logger logger = LoggerFactory.getLogger(StreamAppliacation.class);
    public static void main(String[] args) {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
  env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        DataStream<String> source = env.addSource(
                new FlinkKafkaConsumer010("my_topic", new SimpleStringSchema(), new Properties()))
                .setParallelism(2);

        DataStream<Tuple2<String, Integer>> counts = source
            .flatMap(new WordCount.Tokenizer()).setParallelism(2)
            .keyBy(0).timeWindowAll(Time.seconds(10))
            .sum(1).setParallelism(2);


        counts.addSink(new SinkFunction<Tuple2<String, Integer>>() {
            @Override
            public void invoke(Tuple2<String, Integer> value) throws Exception {
            System.out.println("sink data to log: {word:"+value.f0+",count:"+value.f1);
            }
        }).setParallelism(1);

    }
}
