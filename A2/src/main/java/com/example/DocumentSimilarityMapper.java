package com.example;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.*;

public class DocumentSimilarityMapper extends Mapper<Object, Text, Text, Text> {
	private Text wordText = new Text();
    	private Text docIDText = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String[] parts = value.toString().toLowerCase()split("\\s+");
		if (parts.length < 2) return;

		String docID = parts[0];
		docIDText.set(docID);

		Set<String> uniqueWords = new HashSet<>();
		for (int i = 1; i < parts.length; i++) {
			uniqueWords.add(parts[i]);
		}

		for (String word : uniqueWords) {
			wordText.set(word);
			context.write(wordText, docIDText);
		}
	}	
}
