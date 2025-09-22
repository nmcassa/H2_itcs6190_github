package com.example;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.*;

public class IntersectionReducer extends Reducer<Text, Text, Text, Text> {
	private Map<String, Integer> intersectionCounts = new HashMap<>();
	private Map<String, Integer> docSizes = new HashMap<>();

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String keyStr = key.toString();
		if (keyStr.startsWith("DOCSIZE:")) {
			String docID = keyStr.substring("DOCSIZE:".length());
			int size = Integer.parseInt(values.iterator().next().toString());
			docSizes.put(docID, size);
		} else {
			int sum = 0; 
			for (Text val : values) {
				sum += Integer.parseInt(val.toString().trim());
			}
			intersectionCounts.put(keyStr, sum);
		}
	}

	protected void cleanup(Context context) throws IOException, InterruptedException {
		for (Map.Entry<String, Integer> entry : intersectionCounts.entrySet()) {
			String[] docs = entry.getKey().split(",");
			String doc1 = docs[0];
			String doc2 = docs[1];
			int intersection = entry.getValue();
			int size1 = docSizes.getOrDefault(doc1, 0);
			int size2 = docSizes.getOrDefault(doc2, 0);
			int union = size1 + size2 - intersection;

			double jaccard = (union == 0) ? 0.0 : (double) intersection / union;
			context.write(new Text(doc1 + "," + doc2), new Text(String.format("Similarity %.4f", jaccard)));
		}
	}
}
