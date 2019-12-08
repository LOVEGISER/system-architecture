package com.alex.flink.stream.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
/**
 *
* Title: KafkaConsumerTest
* Description:
*  kafka消费者 demo
* Version:1.0.0
* @author pancm
* @date 2018年1月26日
 */
public class KafkaConsumerTest implements Runnable {
	private final KafkaConsumer<String, String> consumer;
	private ConsumerRecords<String, String> msgList;
	private final String topic;
	private static final String GROUPID = "CG_YEAHDSP_FLINK_";
	public KafkaConsumerTest(String topicName) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "10.8.5.13:9092,10.8.5.14:9092,10.8.105.13:9092");
		props.put("group.id", GROUPID+topicName);
		props.put("enable.auto.commit", "true");
		//props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("auto.offset.reset", "latest");
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		this.consumer = new KafkaConsumer<String, String>(props);
		this.topic = topicName;
		this.consumer.subscribe(Arrays.asList(topic));
	}
	@Override
	public void run() {
		int messageNo = 1;
		System.out.println("---------开始消费---------");
		try {
			for (;;) {
					msgList = consumer.poll(100);
					if(null!=msgList&&msgList.count()>0){

				     	//for (ConsumerRecord<String, String> record : msgList) {
						//消费100条就打印 ,但打印的数据不一定是这个规律的
//						if(messageNo%100==0){
//							System.out.println(messageNo+"=======receive: key = " + record.key() + ", value = " + record.value()+" offset==="+record.offset());
//						}
//						//当消费了1000条就退出
//						if(messageNo%1000==0){
//							break;
//						}
					//	messageNo++;
				 	//}

                        Constant.totalCount= Constant.totalCount+msgList.count();
                       // System.out.println( Constant.totalCount - pre);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}
	public static void main(String args[]) {
	    for(int i = 0 ; i <2;i++){
            KafkaConsumerTest test1 = new KafkaConsumerTest("cpi_response");
            Thread thread1 = new Thread(test1);
            thread1.start();
        }

        TimerTask task = new TimerTask() {
            int pre   = 0;
            @Override
            public void run() {
                // task to run goes here
                // 执行的输出的内容
                System.out.println("rps:" +(Constant.totalCount - Constant.preCount));
                Constant.preCount =  Constant.totalCount;
            }
        };
        Timer timer = new Timer();
        // 定义开始等待时间  --- 等待 1 秒
        // 1000ms = 1s
        long delay = 1000;
        // 定义每次执行的间隔时间
        long intevalPeriod = 1 * 1000;
        // schedules the task to be run in an interval
        // 安排任务在一段时间内运行
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);



	}
}
