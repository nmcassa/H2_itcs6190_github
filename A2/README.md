# Assignment 2: Document Similarity using MapReduce

**Name: Nicholas Cassarino** 

**Student ID: 801177054** 

## Approach and Implementation

### Mapper and Reducer Design
My Implementation of a 'Document Similarity using MapReduce' utilizes two Mappers and Reducers. The first Mapper is present in DocumentSimilarityMapper.java. Here we take in our input which is shaped like this:


```
Document1 This is a sample document containing words
Document2 Another document that also has words
Document3 Sample text with different words
```


 and we map the first word of each line to be the documentID or key in our key-value pair. Then we create a HashSet of all the other words in our line to eliminate all duplicates. Then we one by one create a key value pair for each unique word in each line/document.


```
(this, Document1)
(is, Document1)
...
(different, Document3)
(words, Document3)
```


We pass these key-value pairs to our first reducer which is located in DocumentSimilarityReducer.java. Here we calculate where all of the intersected words within each document are and label them with a one. But also at the end of this reducer we also log the size of each document so that we can interact with the data later on(with the DOCSIZE key start). 


```
("Document1, Document2", 1)
("Document2, Document3", 1)
("Document2, Document3", 1)
...
("DOCSIZE:Document3", 5)
```


 Now we move on to the implementation of the second mapreduce cycle. Here we take the input from the previous cycles output (interm file) and continue to do our computation. Here we use the Mapper located at TextToIntMapper.java. This file name is not so accurate at this point as it just passes the data forwards so that we can reduce once more. In the reduce function, located at IntersectionReducer.java we take this input and first check if our line contains DOCSIZE, if so we can save the information for our cleanup function. Otherwise, we have some data to reduce. So we simply count up all of the intersections that we can find for a "Document?, Document?" pairing and pass this to cleanup. In the cleanup function we use the data that we have gained to calculate and then print our Jaccard Similarity. We calculate the simularity by dividing the intersection of the two documents by the union of them. Then we handsomely print out our data like this:


```
"Document1, Document2 Similarity: 0.5600"
"Document1, Document3 Similarity: 0.4200"
"Document2, Document3 Similarity: 0.5000"
```

### Overall Data Flow
I hope that my explanation above will suffice for this section, I was able to outline how the data is moving from one step to another. Sorts and shuffles can be used between the mapper, reducer, and input in order to facilitate this computation within the structure of the mapreduce model. 

---

## Setup and Execution

### ` Note: The below commands are the ones used for the Hands-on. You need to edit these commands appropriately towards your Assignment to avoid errors. `
I have edited these steps to be consistent with the code present in this directory

### 1. **Start the Hadoop Cluster**

Run the following command to start the Hadoop cluster:

```bash
docker compose up -d
```

### 2. **Build the Code**

Build the code using Maven:

```bash
mvn clean package
```

### 4. **Copy JAR to Docker Container**

Copy the JAR file to the Hadoop ResourceManager container:

```bash
docker cp target/DocumentSimilarity-0.0.1-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 5. **Move Dataset to Docker Container**

Copy the dataset to the Hadoop ResourceManager container:

```bash
docker cp shared-folder/input/data/dataset_<size>.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 6. **Connect to Docker Container**

Access the Hadoop ResourceManager container:

```bash
docker exec -it resourcemanager /bin/bash
```

Navigate to the Hadoop directory:

```bash
cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 7. **Set Up HDFS**

Create a folder in HDFS for the input dataset:

```bash
hadoop fs -mkdir -p /input/data
```

Copy the input dataset to the HDFS folder:

```bash
hadoop fs -put ./dataset_<data_size>.txt /input/data
```

### 8. **Execute the MapReduce Job**

Run your MapReduce job using the following command: Here I got an error saying output already exists so I changed it to output1 instead as destination folder

```bash
hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/DocumentSimilarity-0.0.1-SNAPSHOT.jar com.example.controller.DocumentSimilarityDriver /input/data/input.txt /interm1 /output1
```

### 9. **View the Output**

To view the output of your MapReduce job, use:

```bash
hadoop fs -cat /output1/*
```

### 10. **Copy Output from HDFS to Local OS**

To copy the output from HDFS to your local machine:

1. Use the following command to copy from HDFS:
    ```bash
    hdfs dfs -get /output1 /opt/hadoop-3.2.1/share/hadoop/mapreduce/
    ```

2. use Docker to copy from the container to your local machine:
   ```bash
   exit 
   ```
    ```bash
    docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output1/ shared-folder/output/
    ```
3. Commit and push to your repo so that we can able to see your output


---

## Challenges and Solutions

My first challenge was actually figuring out a way to do this. I know that I probably could have done this with only one Map and Reduce step but it was easier for me to think it through when utilizing two steps. This came with issues though because I needed to learn how to properly write the controller code to handle this, and datatype mismatches became a very common issue. The second challenge was that the template code given created the directory with the path src/main/com/example, this path does not properly compile with the given pom.xml file. This caused me problems because it was very difficult for me to identify the compilation issue. Once, I changed the path to **src/main/java/com/example** it worked. 


Most of my issues were just confusing myself

## Trying Different amounts of nodes

 When using just one node instead of three or two our execution time is longer, especially for large jobs. This is because we lose the parallelism of hadoop. With only one node we aren't able to split up our task and get parallelism. This causes our performance downgrade.

---
## Sample Input

**Input from `small_dataset.txt`**
```
Document1 This is a sample document containing words
Document2 Another document that also has words
Document3 Sample text with different words
```
## Sample Output

**Output from `small_dataset.txt`**
```
"Document1, Document2 Similarity: 0.56"
"Document1, Document3 Similarity: 0.42"
"Document2, Document3 Similarity: 0.50"
```
## Obtained Output: (Place your obtained output here.)
**Input and output for datasets of size [1000, 3000, 5000] can be found in shared-folder/<input, output>**
and the file i used to generate the datasets is there aswell. If I spent some time to add more random words to the datasets the results would be less uniform.


Also, i did this whole assignment in the github codespace thing and it was really nice, but it says that there is only two days left on this so hopefully it isn't down by the time you guys see this! Thanks!
