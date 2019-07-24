package quickstart;
/* SimpleApp.java */
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
 import org.apache.spark.sql.*;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;

    public class SimpleAppJava {
        public static void main(String[] args) {
            String logFile =  "///Users/wangleigis163.com/Documents/alex/dev/code/private/manager-system/spark/src/main/resources/"; // Should be some file on your system
            SparkSession spark = SparkSession.builder().appName("Simple Application").getOrCreate();

            Dataset<String> lines = spark.read().textFile("data.txt");
            long count = lines.count();



            System.out.println("======*****************============Lines with a: " + count );

            spark.stop();
        }
    }
