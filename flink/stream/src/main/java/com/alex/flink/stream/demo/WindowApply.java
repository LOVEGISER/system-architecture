package com.alex.flink.stream.demo;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class WindowApply {
  public static void main(String[] args) throws Exception {
      final String hostname = "127.0.0.1";
      final int port = 8000;
    // 1：定义（获取）Flink执行环境，StreamExecutionEnvironment
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

      // 2：监听并获取Socket服务端口上的数据(数据源流-Source Operator)
      DataStream<String> socketTextStream = env.socketTextStream(hostname, port, "\n");
    socketTextStream
        .timeWindowAll(Time.seconds(10))
        .apply(
            new AllWindowFunction<String, Integer, TimeWindow>() {
              @Override
              public void apply(TimeWindow window, Iterable<String> values, Collector<Integer> out)
                  throws Exception {
                int max = 0;//1：最大值
                int count = 0;//2：统计当前窗口内数据流
                for (Object t : values) {
                  int number = Integer.parseInt(String.valueOf(t));
                    System.out.print(String.format("number:%s;",number ));
                  if (number > max) {
                    max = number;
                  }
                  count ++ ;
                }

                out.collect(new Integer(max));
                System.out.println(String.format("window start:%s:,end:%s,count:%s,maxvalue:%s", window.getStart(),window.getEnd(),count,max));
              }
            })
        .addSink(
            new SinkFunction() {
              @Override
              public void invoke(Object value) throws Exception {
                System.out.println("WindowApply.invoke:" + value);
              }
            });
    // 5：提交并执行任务
    env.execute("WindowApply");
  }
}
