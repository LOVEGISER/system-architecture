package sql;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 Spark SQL是用于结构化数据处理的Spark模块。与基本的Spark RDD API不同，Spark SQL提供的接口为Spark提供了有关数据结构和正在执行的计算的更多信息。在内部，Spark SQL使用此额外信息来执行额外的优化。有几种与Spark SQL交互的方法，包括SQL和Dataset API。在计算结果时，使用相同的执行引擎，与您用于表达计算的API /语言无关。这种统一意味着开发人员可以轻松地在不同的API之间来回切换，从而提供表达给定转换的最自然的方式。

 此页面上的所有示例都使用Spark分发中包含的示例数据，并且可以在spark-shell，pyspark shell或sparkR shell中运行。

 SQL
 Spark SQL的一个用途是执行SQL查询。 Spark SQL还可用于从现有Hive安装中读取数据。有关如何配置此功能的更多信息，请参阅Hive Tables部分。从其他编程语言中运行SQL时，结果将作为数据集/数据框返回。您还可以使用命令行或JDBC / ODBC与SQL接口进行交互。

 数据集和数据框架
 数据集是分布式数据集合。数据集是Spark 1.6中添加的一个新接口，它提供了RDD的优势（强类型，使用强大的lambda函数的能力）和Spark SQL优化执行引擎的优点。数据集可以从JVM对象构造，然后使用功能转换（map，flatMap，filter等）进行操作。数据集API在Scala和Java中可用。 Python没有对Dataset API的支持。但由于Python的动态特性，数据集API的许多好处已经可用（即您可以通过名称自然地访问行的字段row.columnName）。 R的情况类似。

 DataFrame是一个组织成命名列的数据集。它在概念上等同于关系数据库中的表或R / Python中的数据框，但在底层具有更丰富的优化。 DataFrame可以从多种来源构建，例如：结构化数据文件，Hive中的表，外部数据库或现有RDD。 DataFrame API在Scala，Java，Python和R中可用。在Scala和Java中，DataFrame由行数据集表示。在Scala API中，DataFrame只是Dataset [Row]的类型别名。而在Java API中，用户需要使用Dataset <Row>来表示DataFrame。
 */

/**
 *  ./bin/spark-submit  --class "sql.SQLSimple"  /Users/wangleigis163.com/Documents/alex/dev/code/private/spark/target/spark-1.0.jar
 */
public class SQLSimple1 {
    public static void main(String[] args) {
        String jsonpath = "/Users/wangleigis163.com/Documents/alex/dev/code/private/spark/src/main/resources/people.json";
        String textpath = "/Users/wangleigis163.com/Documents/alex/dev/code/private/spark/src/main/resources/people.txt";

        SparkSession spark = SparkSession
                .builder()
                .appName("Spark SQL example")
                //.config("spark.some.config.option", "some-value")
                .getOrCreate();
        /**
         part1：
         使用SparkSession，应用程序可以从现有RDD，Hive表或Spark数据源创建DataFrame。

         作为示例，以下内容基于JSON文件的内容创建DataFrame：
         */
        Dataset<Row> df = spark.read().json(jsonpath);

       // Displays the content of the DataFrame to stdout
        df.show();

        // Print the schema in a tree format
                df.printSchema();
        // root
        // |-- age: long (nullable = true)
        // |-- name: string (nullable = true)

        // Select only the "name" column
                df.select("name").show();
        // +-------+
        // |   name|
        // +-------+
        // |Michael|
        // |   Andy|
        // | Justin|
        // +-------+





        // Count people by age
        df.groupBy("age").count().show();
        // +----+-----+
        // | age|count|
        // +----+-----+
        // |  19|    1|
        // |null|    1|
        // |  30|    1|
        // +----+-----+
        /**
         以编程方式运行SQL查询
         SparkSession上的sql函数使应用程序能够以编程方式运行SQL查询，并将结果作为数据集<Row>返回。
         */
        df.createOrReplaceTempView("people");

        Dataset<Row> sqlDF = spark.sql("SELECT *  FROM people where age > 20 and to_unix_timestamp(time1, 'yyyy-MM-dd HH:mm:ss.SSS') - to_unix_timestamp(time, 'yyyy-MM-dd HH:mm:ss.SSS')>0");
        //Dataset<Row> sqlDF = spark.sql("SELECT * FROM people where age > 20");
        sqlDF.show();


    }
}
