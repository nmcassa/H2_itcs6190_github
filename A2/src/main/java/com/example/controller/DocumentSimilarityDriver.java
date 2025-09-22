package com.example.controller;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;

import com.example.DocumentSimilarityMapper;
import com.example.DocumentSimilarityReducer;
import com.example.IntersectionReducer;
import com.example.TextToIntMapper;

public class DocumentSimilarityDriver {
    
    public static void main(String[] args) throws Exception {
    	if (args.length != 3) {
		System.err.println("Usage: SimularityController <input> <intermediate-output> <final-output>");
		System.exit(2);
	}	

	String inputPath = args[0];
	String interimPath = args[1];
	String finalPath = args[2];

	Configuration conf = new Configuration();

	//Job 1
	Job job1 = Job.getInstance(conf, "Document Similarity - Partial Intersection");
	job1.setJarByClass(DocumentSimilarityDriver.class);
	job1.setMapperClass(DocumentSimilarityMapper.class);
	job1.setReducerClass(DocumentSimilarityReducer.class);
	job1.setMapOutputKeyClass(Text.class);
	job1.setMapOutputValueClass(Text.class);
	job1.setOutputKeyClass(Text.class);
	job1.setOutputValueClass(IntWritable.class);

	FileInputFormat.addInputPath(job1, new Path(inputPath));
	FileOutputFormat.setOutputPath(job1, new Path(interimPath));

	boolean success = job1.waitForCompletion(true);
	if (!success) {
		System.err.println("Job1 Failed");
		System.exit(1);
	}

	// Job 2
	Job job2 = Job.getInstance(conf, "Document Similarity - Sum Intersections");
	job2.setJarByClass(DocumentSimilarityDriver.class);
	job2.setMapperClass(TextToIntMapper.class);
	job2.setReducerClass(IntersectionReducer.class);
	job2.setMapOutputKeyClass(Text.class);
	job2.setMapOutputValueClass(IntWritable.class);
	job2.setInputFormatClass(KeyValueTextInputFormat.class);
	FileInputFormat.addInputPath(job2, new Path(interimPath));
	FileOutputFormat.setOutputPath(job2, new Path(finalPath));

	success = job2.waitForCompletion(true);
	if (!success) {
		System.err.println("Job2 failed");
		System.exit(1);
	}

	System.exit(0);
    }
}
