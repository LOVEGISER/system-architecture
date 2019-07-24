package structuredstreaming;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.Arrays;

/*
./bin/spark-submit  --class "structuredstreaming.Simple"  /Users/wangleigis163.com/Documents/alex/dev/code/private/spark/target/spark-1.0.jar

     */
public class Simple {
    /**
     StructuredStreaming-结构化流是一种基于Spark SQL引擎的可扩展且容错的流处理引擎。您可以像表达静态数据的批处理计算一样表达流式计算。 Spark SQL引擎将负责逐步和连续地运行它，并在流数据继续到达时更新最终结果。您可以使用Scala，Java，Python或R中的数据集/数据框架API来表示流聚合，事件时间窗口，流到批处理连接等。计算在同一优化的Spark SQL引擎上执行。最后，系统通过检查点和预写日志确保端到端的一次性容错保证。简而言之，Structured Streaming提供快速，可扩展，容错，端到端的精确一次流处理，而无需用户推理流式传输。

     在内部，默认情况下，结构化流式查询使用微批处理引擎进行处理，该引擎将数据流作为一系列小批量作业处理，从而实现低至100毫秒的端到端延迟和完全一次的容错保证。但是，自Spark 2.3以来，我们引入了一种称为连续处理的新型低延迟处理模式，它可以实现低至1毫秒的端到端延迟，并且具有至少一次保证。无需更改查询中的数据集/数据框操作，您就可以根据应用程序要求选择模式。
     * @param args
     */
    public static void main(String[] args) {
        /**
         通过侦听TCP数据服务器端口上的数据并处理。 具体Structured Streaming使用如下。
         */
        SparkSession spark = SparkSession
                .builder()
                .appName("StructuredNetworkWordCount")
                .getOrCreate();
        // Create DataFrame representing the stream of input lines from connection to localhost:9999
        Dataset<Row> lines = spark
                .readStream()
                .format("socket")
                .option("host", "localhost")
                .option("port", 9999)
                .load();

     // Split the lines into words
        Dataset<String> words = lines
                .as(Encoders.STRING())
                .flatMap((FlatMapFunction<String, String>) x -> Arrays.asList(x.split(" ")).iterator(), Encoders.STRING());

     // Generate running word count
        Dataset<Row> wordCounts = words.groupBy("value").count();
        System.out.println("wordCounts:"+wordCounts);
        /**
         代码中lines 的DataFrame表示包含流文本数据的无界表。此表包含一列名为“value”的字符串，并且流式文本数据中的每一行都成为表中的一行。请注意，由于我们只是设置转换，并且尚未启动它，因此目前没有接收任何数据。接下来，我们使用.as（Encoders.STRING（））将DataFrame转换为String的数据集，以便我们可以应用flatMap操作将每行拆分为多个单词。生成的单词Dataset包含所有单词。最后，我们通过对数据集中的唯一值进行分组并对它们进行计数来定义wordCounts DataFrame。请注意，这是一个流式DataFrame，它表示流的运行字数。
         我们现在已经设置了关于流数据的查询。剩下的就是实际开始接收数据并计算计数。为此，我们将其设置为每次更新时将完整的计数集（由outputMode（“complete”）指定）打印到控制台。然后使用start（）开始流式计算。
         */
        // Start running the query that prints the running counts to the console
        StreamingQuery query = wordCounts.writeStream()
                .outputMode("complete")
                .format("console")
                .start();

        try {
            query.awaitTermination();
        } catch (StreamingQueryException e) {
            e.printStackTrace();
        }
        /**
         执行此代码后，流式计算将在后台启动。 query对象是该活动流式查询的句柄，我们决定使用awaitTermination（）等待查询终止，以防止进程在查询处于活动状态时退出。

         使用Netcat（在大多数类Unix系统中找到的小实用程序）作为数据服务器运行:nc -lk 9999

         Batch: 1
         -------------------------------------------
         19/06/01 17:31:26 INFO CodeGenerator: Code generated in 6.993496 ms
         +-----+-----+
         |value|count|
         +-----+-----+
         |hello|    1|
         | java|    1|
         +-----+-----+



         19/06/01 17:29:32 INFO MicroBatchExecution: Streaming query made progress: {
         "id" : "0169fdc8-51c0-4293-b3ec-dbc24b360540",
         "runId" : "88da4bae-3013-4e8e-bb6f-dad1e85ca36a",
         "name" : null,
         "timestamp" : "2019-06-01T09:29:32.038Z",
         "batchId" : 5,
         "numInputRows" : 0,
         "inputRowsPerSecond" : 0.0,
         "durationMs" : {
         "getEndOffset" : 0,
         "setOffsetRange" : 0,
         "triggerExecution" : 0
         },
         "stateOperators" : [ {
         "numRowsTotal" : 8,
         "numRowsUpdated" : 0,
         "memoryUsedBytes" : 81567,
         "customMetrics" : {
         "loadedMapCacheHitCount" : 1600,
         "loadedMapCacheMissCount" : 0,
         "stateOnCurrentVersionSizeBytes" : 18871
         }
         } ],
         "sources" : [ {
         "description" : "TextSocketV2[host: localhost, port: 9999]",
         "startOffset" : 3,
         "endOffset" : 3,
         "numInputRows" : 0,
         "inputRowsPerSecond" : 0.0
         } ],
         "sink" : {
         "description" : "org.apache.spark.sql.execution.streaming.ConsoleSinkProvider@153a7c0"
         }
         }


         */

    }
}
