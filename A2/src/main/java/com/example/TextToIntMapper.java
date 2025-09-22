package com.example;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.*;

public class TextToIntMapper extends Mapper<Text, Text, Text, IntWritable> {
	private final IntWritable count = new IntWritable();

	protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
		try {
			int val = Integer.parseInt(value.toString().trim());
			count.set(val);
			context.write(key, count);
		}
		catch (NumberFormatException e) {

		}
	}
}
