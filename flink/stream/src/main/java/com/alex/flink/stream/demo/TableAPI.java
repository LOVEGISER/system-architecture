package com.alex.flink.stream.demo;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.Tumble;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.util.Collector;

import java.util.Properties;

public class TableAPI {
  public static void main(String[] args) throws Exception {

      // 1：定义StreamExecutionEnvironment
      StreamExecutionEnvironment bsEnv = StreamExecutionEnvironment.getExecutionEnvironment();
      EnvironmentSettings bsSettings = EnvironmentSettings.newInstance().build();
      // 2：定义StreamTableEnvironment实例
      StreamTableEnvironment tableEnv = StreamTableEnvironment.create(bsEnv, bsSettings);



      try {
          // 3：定义kafka数据源
//          Properties properties = new Properties();
//          properties.setProperty("bootstrap.servers", "localhost:9092");
//          properties.setProperty("group.id", "test");
//          FlinkKafkaConsumer011<String> myConsumer = new FlinkKafkaConsumer011<>(
//                  "my-topic",new SimpleStringSchema(),properties);

          final int port = 8000;
          // 1：定义（获取）Flink执行环境，StreamExecutionEnvironment
          final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
          // 2：监听并获取Socket服务端口上的数据(数据源流-Source Operator)
          DataStream<String> sourceStream = env.socketTextStream("127.0.0.1", port, "\n");


////4：向StreamExecutionEnvironment中添加数据源
//          DataStream<String> sourceStream = bsEnv.addSource(text);
          //5:将Kafka接收的数据转换为Persion格式
          DataStream<Persion> dataStream = sourceStream.flatMap(new FlatMapFunction<String, Persion>() {
              @Override
              public void flatMap(String str, Collector<Persion> collector) throws Exception {
                  Persion persion  = JSON.parseObject(str,Persion.class);
                  collector.collect(persion);
              }
          });
          //6：注册Persion 表（Table）到Catalog上
          tableEnv.registerDataStream("Persion", dataStream);
          //7：创建一个Table API 的查询，查询结果为Table
          Table tapiResult =tableEnv.scan("Persion").filter("id>5").groupBy("id").select("id,age");

          //7：创建一个SQL API 的查询，查询结果为Table
          Table sqlResult = tableEnv.sqlQuery("SELECT id, age from Persion where id>5");
          //8：将执行结果转换为DataStream
          DataStream<String> tableAPIStream = tableEnv.toAppendStream(tapiResult, String.class);
          DataStream<String> sqlAPIStream = tableEnv.toAppendStream(sqlResult, String.class);
          sqlAPIStream.timeWindowAll(Time.seconds(10));
          //9：执行结果打印输出
          tableAPIStream.print();
          sqlAPIStream.print();
          bsEnv.execute();
      } catch (Exception e) {
          e.printStackTrace();
      }

  }
}
