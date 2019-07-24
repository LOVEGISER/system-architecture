package com.alex.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 相当于一个yarn集群的客户端
 * 需要在此封装我们的mr程序的相关运行参数，指定jar包
 * 最后提交给yarn
 * @author
 *
 */
public class WordcountDriver {

	public static void main(String[] args) throws Exception {



		if (args == null || args.length == 0) {
			args = new String[2];
			args[0] = "hdfs://127.0.0.1:9000/input/";
			args[1] = "hdfs://127.0.0.1:9000/output/"+System.currentTimeMillis();
		}

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		//指定本程序的jar包所在的本地路径
		//job.setJarByClass(WordcountDriver.class);
		//指定需要使用combiner，以及用哪个类作为cominer的逻辑，，其实WordCountCombiner 就是WordCountReducer
		/*job.setCombinerClass(WordCountCombiner.class);*/
//		job.setCombinerClass(WordcountReducer.class);

		//如果小文件过多，可以设置，让小文件放入一个task里
		job.setInputFormatClass(CombineTextInputFormat.class);
		CombineTextInputFormat.setMaxInputSplitSize(job, 4194304);//切片最大4M，根据业务可以进行设置
		CombineTextInputFormat.setMinInputSplitSize(job, 2097152);//切片最小2M


		//指定本业务job要使用的mapper/Reducer业务类
		job.setMapperClass(WordcountMapper.class);
		job.setReducerClass(WordcountReducer.class);

		//指定mapper输出数据的kv类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		//指定最终输出的数据的kv类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		//指定job的输入原始文件所在目录
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		//指定job的输出结果所在目录
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		//将job中配置的相关参数，以及job所用的java类所在的jar包，提交给yarn去运行
//		job.submit();
		boolean res = job.waitForCompletion(true);
		System.exit(res?0:1);

	}


}
