package com.example;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.*;

public class DocumentSimilarityReducer extends Reducer<Text, Text, Text, IntWritable> {
	private Text docPair = new Text();
    	private final static IntWritable one = new IntWritable(1);	
	private Map<String, Integer> docSizes = new HashMap<>();

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		List<String> docs = new ArrayList<>();
		for (Text val : values) {
			docs.add(val.toString());
		}

		for (String doc : docs) {
			docSizes.put(doc, docSizes.getOrDefault(doc, 0) + 1);
		}

		for (int i = 0; i < docs.size(); i++) {
			for (int j = i + 1; j < docs.size(); j++) {
				String doc1 = docs.get(i);
				String doc2 = docs.get(j);
				//get pairs (doc1, doc2) 
				//	always puts the lower doc# at the start so that we don't get duplicates
				String pairKey = doc1.compareTo(doc2) < 0 ? doc1 + "," + doc2 : doc2 + "," + doc1;
				docPair.set(pairKey);
				context.write(docPair, one);
			}
		}
	}

	protected void cleanup(Context context) throws IOException, InterruptedException {
		for (Map.Entry<String, Integer> entry : docSizes.entrySet()) {
			String docID = entry.getKey();
			int size = entry.getValue();
			context.write(new Text("DOCSIZE:" + docID), new IntWritable(size));
		}
	}
}


