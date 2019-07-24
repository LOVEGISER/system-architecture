package streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;
/*

./bin/spark-submit  --class "streaming.Streaming"  /Users/wangleigis163.com/Documents/alex/dev/code/private/spark/target/spark-1.0.jar

 */
public class Streaming {
    public static void main(String[] args) {
// Create a local StreamingContext with two working thread and batch interval of 1 second
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
        // Create a DStream that will connect to hostname:port, like localhost:9999
        //使用此上下文，我们可以创建一个DStream来表示来自TCP源的流数据，指定为主机名（例如localhost）和端口
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999);
        // Split each line into words
        //ines DStream表示将从数据服务器接收的数据流。 此流中的每条记录都是一行文本。 然后，我们想要将空格分割为单词。
        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
       //flatMap是一种DStream操作，它通过从源DStream中的每个记录生成多个新记录来创建新的DStream。 在这种情况下，每行将被分成多个单词，单词流表示为单词DStream。 请注意，我们使用FlatMapFunction对象定义了转换。 正如我们将要发现的那样，Java API中有许多这样的便利类可以帮助定义DStream转换。
        // Count each word in each batch
        //
        JavaPairDStream<String, Integer> pairs = words.mapToPair(s -> new Tuple2<>(s, 1));
        JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey((i1, i2) -> i1 + i2);
        System.out.println("wordCounts:"+wordCounts);
    // Print the first ten elements of each RDD generated in this DStream to the console
        wordCounts.print();

        //使用PairFunction对象将字DStream进一步映射（一对一变换）到（字，1）对的DStream。 然后，使用Function2对象将其缩小以获得每批数据中的单词频率。 最后，wordCounts.print（）将打印每秒生成的一些计数。
        //
        //请注意，执行这些行时，Spark Streaming仅设置它在启动后将执行的计算，并且尚未启动实际处理。 要在设置完所有转换后开始处理，我们最后调用start方法。
        jssc.start();              // Start the computation
        try {
            jssc.awaitTermination();   // Wait for the computation to terminate
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
