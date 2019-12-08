package com.alex.flink.stream.kafka;


import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.operators.StreamingRuntimeContext;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;

public class KafkaConsumer {

//    static {
//        try {
//            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//            JoranConfigurator jconfig = new JoranConfigurator();
//            jconfig.setContext(lc);
//            lc.reset();
//            jconfig.doConfigure(KafkaConsumer.class.getClassLoader().getResourceAsStream("logback.xml"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


    public static void main(String[] args) throws Exception {


        // set up the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000); // checkpoint every 5000 msecs

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "10.8.5.13:9092,10.8.5.14:9092,10.8.105.13:9092");
        // only required for Kafka 0.8
        //properties.setProperty("zookeeper.connect", "localhost:2181");
        properties.setProperty("group.id", "CG_YEAHDSP_FLINK_save_event");

        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
                "save_event", new SimpleStringSchema(), properties);



      env.addSource(new YMKafkaSource("save_event"))
     //   env.addSource(consumer)
                .process(new ProcessMessage())
                .addSink(new KafkaSink());
//
//        Properties props = new Properties();
//      //  props.setProperty("zookeeper.connect", "localhost:56794");
//        props.setProperty("bootstrap.servers", "10.8.5.13:9092,10.8.5.14:9092,10.8.105.13:9092");
//        props.setProperty("group.id", "CG_YEAHDSP_FLINK_save_event");
       // props.setProperty(FlinkKafkaConsumer.DEFAULT_POLL_TIMEOUT, 100);

//        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
//                "save_event", new SimpleStringSchema(), props);
//        StreamingRuntimeContext mockRuntimeContext = mock(StreamingRuntimeContext.class);
//        Mockito.when(mockRuntimeContext.isCheckpointingEnabled()).thenReturn(true);
//        consumer.setRuntimeContext(mockRuntimeContext);
//
//        consumer.open(new Configuration());



        env.execute("Kafka");

    }

}
