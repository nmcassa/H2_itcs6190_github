package com.example;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.*;

public class IntersectionReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
	private Map<String, Integer> intersectionMap = new HashMap<>();

	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0; 
		for (IntWritable val : values) {
			sum += val.get();
		}
		intersectionMap.put(key.toString(), sum);
	}

	protected void cleanup(Context context) throws IOException, InterruptedException {
		List<Map.Entry<String, Integer>> entries = new ArrayList<>(intersectionMap.entrySet());
		entries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

		for (Map.Entry<String, Integer> e : entries) {
			context.write(new Text(e.getKey()), new IntWritable(e.getValue()));
		}
	}
}
