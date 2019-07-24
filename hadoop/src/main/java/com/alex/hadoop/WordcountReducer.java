package com.alex.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WordcountReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	 // 入参key，是一组相同单词kv对的key.如：<A,1><B,1>
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int count=0;
		for(IntWritable value:values){
			count += value.get();
		}
		context.write(key, new IntWritable(count));

	}
}
