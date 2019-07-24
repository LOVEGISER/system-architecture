package com.alex.flink.stream.ymclick;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContinueTriggerDemo {
    final  static Logger logger = LoggerFactory.getLogger(ContinueTriggerDemo.class);
    public static void loadCofig() {
        try {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator jconfig = new JoranConfigurator();
            jconfig.setContext(lc);
            lc.reset();
            jconfig.doConfigure(ContinueTriggerDemo.class.getClassLoader().getResourceAsStream("logback.xml"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
       loadCofig();
        String hostName = "localhost";
        Integer port = Integer.parseInt("8001");
        // set up the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        env.enableCheckpointing(1000 * 10 , CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(1000*1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        // 从指定socket获取输入数据
        DataStream<String> text = env.socketTextStream(hostName, port);
       // DataStream<String> text = env.addSource(new ApolloSource(""));

        /*text.flatMap(new LineSplitter()) //数据语句分词
                .keyBy(0) // 流按照单词分区
                .window(TumblingProcessingTimeWindows.of(Time.seconds(20)))// 设置一个120s的滚动窗口
                .trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(10)))//窗口每统计一次当前计算结果
                .sum(1)// count求和
                .map(new Mapdemo())//输出结果加上时间戳
                .print();*/
        /*text.flatMap(new LineSplitter()) //数据语句分词
                .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple3<String, Integer, Long>>() {
                    @Nullable
                    @Override
                    public Watermark getCurrentWatermark() {
                        return null;
                    }

                    @Override
                    public long extractTimestamp(Tuple3<String, Integer, Long> element, long previousElementTimestamp) {
                        return element.f2;
                    }
                })
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(20)))// 设置一个20s的滚动窗口
                .trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(10)))//窗口每10s统计一次当前计算结果
                .allowedLateness(Time.seconds(5))
                .sum(1)// count求和
                .map(new Mapdemo())//输出结果加上时间戳
                .print();*/

        text.flatMap(new LineSplitter())
                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, Integer, Long>>(Time.seconds(5)) {
                    @Override
                    public long extractTimestamp(Tuple3<String, Integer, Long> element) {
                        return element.f2;
                    }
                })
                .windowAll(TumblingEventTimeWindows.of(Time.minutes(20)))
                //.trigger(ContinuousProcessingTimeTrigger.of(Time.seconds(10)))//窗口每10s统计一次当前计算结果
                //.trigger(PurgingTrigger.of(ContinuousProcessingTimeTrigger.of(Time.seconds(10))))
                .trigger(ContinuousProcessingTimeTrigger1.of(Time.seconds(10)))
                .reduce(new TextReduceFunction())
                .map(new Mapdemo())
                .print()
                //.addSink(new DdjRocketMQSink())
                ;

        env.execute("Java WordCount from SocketTextStream Example");

    }

    /**
     * Implements the string tokenizer that splits sentences into words as a
     * user-defined FlatMapFunction. The function takes a line (String) and
     * splits it into multiple pairs in the form of "(word,1)" (Tuple2<String,
     * Integer>).
     */
    public static final class LineSplitter implements FlatMapFunction<String, Tuple3<String, Integer, Long>> {
        @Override
        public void flatMap(String value, Collector<Tuple3<String, Integer, Long>> out) {
            // normalize and split the line
            String[] tokens = value.toLowerCase().split("\\W+");

            // emit the pairs
            for (String token : tokens) {
                if (token.length() > 0) {
                    logger.info("receive massage:"+token);
                    //out.collect(new Tuple3<String, Integer, Long>(token, 1, new Date().getTime()- RandomUtils.nextInt(1000, 5000)));
                    out.collect(new Tuple3<String, Integer, Long>(token, 1, new Date().getTime()));
                }
            }
        }
    }


    public static final class Mapdemo implements MapFunction<Tuple3<String, Integer, Long>, Tuple3<String, String, Integer>> {
        @Override
        public Tuple3<String, String, Integer> map(Tuple3<String, Integer, Long> value) throws Exception {
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s = format2.format(new Date());
            logger.info("map massage:"+s);
            logger.error("map massage test error log:"+s);
            return new Tuple3<String, String, Integer>(value.f0, s, value.f1);
        }
    }
}
