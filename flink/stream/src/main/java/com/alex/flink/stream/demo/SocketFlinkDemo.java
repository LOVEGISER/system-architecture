package com.alex.flink.stream.demo;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class SocketFlinkDemo {
  public static void main(String[] args) throws Exception {
    // 待监听的Socket的服务地址和端口号
    final String hostname = "127.0.0.1";
    final int port = 8000;
    // 1：定义（获取）Flink执行环境，StreamExecutionEnvironment
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    // 2：监听并获取Socket服务端口上的数据(数据源流-Source Operator)
    DataStream<String> text = env.socketTextStream(hostname, port, "\n");

    // 3：数据装换（计算）流
    DataStream<WordWithCount> windowCounts =
        text.flatMap(
                new FlatMapFunction<
                    String, WordWithCount>() { // 3.1：遍历每个数据，并获取数据内容，构造WordWithCount数据结构并输出
                  @Override
                  public void flatMap(String value, Collector<WordWithCount> out) {
                    for (String word : value.split("\\s")) {
                      out.collect(new WordWithCount(word, 1L));
                    }
                  }
                })
            .keyBy("word") // 3.2：以word字段为key进行分组
            .timeWindow(Time.seconds(5)) // 3.3：定义窗一个间隔为5s的时间窗口（5s触发一次计算）
            .reduce(
                new ReduceFunction<WordWithCount>() { // 3.4：对单词执行统计计算，并返回计算结果
                  @Override
                  public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                    return new WordWithCount(a.word, a.count + b.count);
                  }
                });

    // 4：定义数据输出：获取并打印结果
    windowCounts.addSink(
        new SinkFunction<WordWithCount>() {
          @Override
          public void invoke(WordWithCount value) throws Exception {
            System.out.println("result:" + value.toString());
          }
        });
    // 5：提交并执行任务
    env.execute("SocketFlinkDemo");
  }

  /** Data type for words with count. */
  public static class WordWithCount {

    public String word;
    public long count;

    public WordWithCount() {}

    public WordWithCount(String word, long count) {
      this.word = word;
      this.count = count;
    }

    @Override
    public String toString() {
      return word + " : " + count;
    }
  }
}
