package com.alex.flink.stream.demo;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.io.FileInputFormat;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class DataSetAPI {
  public static void main(String[] args) throws Exception {

    // 1：定义（获取）Flink执行环境，ExecutionEnvironment
    final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

      // 2：从本地文件中读取数据(数据源流-Source Operator)
      DataSet<String> textDataSet = env.readTextFile("file:////Users/wangleigis163.com/Documents/alex/dev/code/private/system-architecture/flink/resources/data.txt");
      textDataSet.filter(new FilterFunction<String>() {
          @Override // 3：过滤偶数
          public boolean filter(String data) throws Exception {
              int value =  Integer.parseInt(data);
              return value%2 == 0;//
          }
      });
      //4：打印计算结果
      textDataSet.print();
     // textDataSet.writeAsText("filepath");
  }
}
