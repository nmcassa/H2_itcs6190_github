# Hands on L4

## Project Overview
We cloned this submodule that allows us to run a MapReduce function with hadoop. The application is to count the number of times we see each word.

## Approach and Implementation
The MapReduce will be distributed and first start by partitioning the text to different processors. Then each processor is going to be creating a key value pair for each word. We then shuffle these by passing each processor all the same word (or groupings of multiple words). Then we reduce these individual key value pairs into one key value pair that has the correct value (the number of times that word appeared). We finally gather all of our key value pairs together and push them to our output file.

## Execution Steps
I followed the steps that were available in the readme from the other repo. But I can also just paste them here again.

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
docker cp target/WordCountUsingHadoop-0.0.1-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 5. **Move Dataset to Docker Container**

Copy the dataset to the Hadoop ResourceManager container:

```bash
docker cp shared-folder/input/data/input.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
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
hadoop fs -put ./input.txt /input/data
```

### 8. **Execute the MapReduce Job**

Run your MapReduce job using the following command: Here I got an error saying output already exists so I changed it to output1 instead as destination folder

```bash
hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/WordCountUsingHadoop-0.0.1-SNAPSHOT.jar com.example.controller.Controller /input/data/input.txt /output1
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

## Challenges Faced & Solutions
The most difficult challenges for me were (1) Correctly setting up the correct java version, and then (2) downloading and using Maven. Especially since I am on macOS and the instructions were for windows. And then because my laptop is old and not M1. I was able to just mess around with paths and downloads and environment variables until some weird combination magically worked. 

## Input and Obtained Output
```
I am in class working
I am learning
```

```
I 2
am 2
learning 1
in 1
class 1
working 1
```
