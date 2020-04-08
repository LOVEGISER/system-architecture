package com.alex.hudi;

import com.google.common.base.Stopwatch;
import org.apache.avro.Schema;
import org.apache.hudi.DataSourceReadOptions;
import org.apache.hudi.DataSourceWriteOptions;
import org.apache.hudi.common.model.HoodieRecord;
import org.apache.hudi.config.HoodieWriteConfig;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.List;


/**
 * @Title: SparkDudi
 * @Package: com.yeahmobi.hudi
 * @Description: TODO
 * @author: Mo.Lee
 * @date: 2020/4/8
 * @since: 1.0
 */
public class SparkDudi {
    public static Logger LOG = Logger.getLogger(SparkDudi.class);

    public static final String tableName = "hudi_table";
    public static final String basePath =  "/Users/wangleigis163.com/Documents/alex/dev/code/private/system-architecture/spark-hudi/src/main/resources/data/";


    public static void main(String[] args) throws Exception {
        Logger.getRootLogger().setLevel(Level.INFO);
        Stopwatch stopwatch = Stopwatch.createStarted();
        SparkSession spark = null;
        try {
            spark = SparkSession.builder().appName(SparkDudi.class.getSimpleName())
                    .config("spark.sql.sources.partitionColumnTypeInference.enabled", "false")
                    .config("spark.eventLog.enabled", true)
                    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                    .config("spark.sql.parquet.binaryAsString", true)
                    .config("spark.sql.warehouse.dir", "E:/tmp/spark-warehouse")
                    .config("spark.driver.memory", "1g")
                    .config("spark.executor.memory", "2g")
                    .config("spark.sql.shuffle.partitions", "20")
                    .config("spark.default.parallelism", "60")
                    .master("local[*]")
                    .getOrCreate();
            spark.sparkContext().setLogLevel("error");
            LOG.info("1.Load Data==================================================================");

        //   insert(spark);
           //update(spark);

            Dataset<Row> hudiIncQueryDF = spark.read()
                    .format("org.apache.hudi")
                    .option(DataSourceReadOptions.QUERY_TYPE_OPT_KEY(), DataSourceReadOptions.QUERY_TYPE_INCREMENTAL_OPT_VAL())
                    .option(DataSourceReadOptions.BEGIN_INSTANTTIME_OPT_KEY(), 20200408181526L)
                    .option(DataSourceReadOptions.END_INSTANTTIME_OPT_KEY(), 20200409181529L)
                    //.option(DataSourceReadOptions.INCR_PATH_GLOB_OPT_KEY(), "/year=2020/month=*/day=*") // Optional, use glob pattern if querying certain partitions
                    .load(basePath); // For incremental query, pass in the root/base path of table
            hudiIncQueryDF.printSchema();
            hudiIncQueryDF.select("fare").show(30, false);

            //hudiIncQueryDF.createOrReplaceTempView("hudi_trips_incremental");
            //spark.sql("select `_hoodie_commit_time`, fare, begin_lon, begin_lat, ts from  hudi_trips_incremental").show();

        } finally {
            spark.stop();
        }
    }


    static void update(SparkSession spark) {
        Dataset<Row> df = spark.read()
                .format("org.apache.hudi")
                .option(DataSourceReadOptions.QUERY_TYPE_OPT_KEY(), DataSourceReadOptions.QUERY_TYPE_INCREMENTAL_OPT_VAL())
                .option(DataSourceReadOptions.BEGIN_INSTANTTIME_OPT_KEY(), 20200408181526L)
                .option(DataSourceReadOptions.END_INSTANTTIME_OPT_KEY(), 20200408181529L)
                .load(basePath);
        df.filter("uuid in('aacd5eb3-0fb7-48bb-ac74-8eea45352d1d','40fc19a8-7959-406d-9015-371c39d7db06')")
                .withColumn("fare", functions.lit(200.0))
                .drop("_hoodie_commit_time", "_hoodie_commit_seqno","_hoodie_record_key","_hoodie_partition_path","_hoodie_file_name")
                .write()
                .format("org.apache.hudi")
                .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY(), "uuid")
                .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY(), "partitionpath")
                .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY(), "ts")
                .option(HoodieWriteConfig.TABLE_NAME, tableName)
                .mode(SaveMode.Append)
                .save(basePath);
        ;
    }

    static void insert(SparkSession spark) throws Exception {
        QuickstartUtils.DataGenerator dataGenerator = new QuickstartUtils.DataGenerator();
        List<HoodieRecord> list = dataGenerator.generateInserts(10);
        List<String> records1 = QuickstartUtils.convertToStringList(list);
        JavaSparkContext jssc = new JavaSparkContext(spark.sparkContext());
        Dataset<Row> dataFrame = spark.read().json(jssc.parallelize(records1, 2));
        dataFrame.printSchema();
        dataFrame.show(10, false);
        dataFrame.write()
                .format("org.apache.hudi")
                //.options(clientOpts) // any of the Hudi client opts can be passed in as well
                .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY(), "uuid")
                .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY(), "partitionpath")
                .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY(), "ts")
                .option(HoodieWriteConfig.TABLE_NAME, tableName)
                .mode(SaveMode.Append)
                .save(basePath);
    }
}
