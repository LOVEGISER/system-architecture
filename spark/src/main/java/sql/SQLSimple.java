package sql;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Serializable;
import org.apache.spark.api.java.function.Function;
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
public class SQLSimple {
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

        Dataset<Row> sqlDF = spark.sql("SELECT * FROM people where age > 20");
        sqlDF.show();
        /**
         全局临时视图
         Spark SQL中的临时视图是会话范围的，如果创建它的会话终止，它将消失。 如果您希望拥有一个在所有会话之间共享的临时视图并保持活动状态，直到Spark应用程序终止，您可以创建一个全局临时视图。 全局临时视图与系统保留的数据库global_temp绑定，我们必须使用限定名称来引用它，例如 SELECT * FROM global_temp.view1。
         */
        // Register the DataFrame as a global temporary view
        try {
            df.createGlobalTempView("people");
        } catch (AnalysisException e) {
            e.printStackTrace();
        }

        // Global temporary view is tied to a system preserved database `global_temp`
                spark.sql("SELECT * FROM global_temp.people").show();
        // +----+-------+
        // | age|   name|
        // +----+-------+
        // |null|Michael|
        // |  30|   Andy|
        // |  19| Justin|
        // +----+-------+

        // Global temporary view is cross-session
                spark.newSession().sql("SELECT * FROM global_temp.people").show();
        // +----+-------+
        // | age|   name|
        // +----+-------+
        // |null|Michael|
        // |  30|   Andy|
        // |  19| Justin|
        // +----+-------+
        /**


         创建数据集
         数据集与RDD类似，但是，它们不使用Java序列化或Kryo，而是使用专用的编码器来序列化对象以便通过网络进行处理或传输。 虽然encoders和标准序列化都负责将对象转换为字节，但encoders是动态生成的代码，并使用一种格式，允许Spark执行许多操作，如过滤，排序和散列，而无需将字节反序列化为对象。
         */

       // Create an instance of a Bean class
        Person person1 = new Person();
        person1.setAge(30);
        person1.setName("alex");



       // Encoders are created for Java beans
        Encoder<Person> personEncoder = Encoders.bean(Person.class);
        Dataset<Person> javaBeanDS = spark.createDataset(
               Collections.singletonList(person1),
                personEncoder
        );
        javaBeanDS.show();


          // Encoders for most common types are provided in class Encoders
        Encoder<Integer> integerEncoder = Encoders.INT();
        Dataset<Integer> primitiveDS = spark.createDataset(Arrays.asList(1, 2, 3), integerEncoder);
        Dataset<Integer> transformedDS = primitiveDS.map(
                (MapFunction<Integer, Integer>) value -> value + 1,
                integerEncoder);
        transformedDS.collect(); // Returns [2, 3, 4]

       // DataFrames can be converted to a Dataset by providing a class. Mapping based on name
        Dataset<Person> peopleDS = spark.read().json(jsonpath).as(personEncoder);
        peopleDS.show();
        /**
         与RDD互操作
         Spark SQL支持两种不同的方法将现有RDD转换为数据集。 第一种方法使用反射来推断包含特定类型对象的RDD的模式。 这种基于反射的方法可以提供更简洁的代码，并且在您编写Spark应用程序时已经了解模式时可以很好地工作。

         创建数据集的第二种方法是通过编程接口，允许您构建模式，然后将其应用于现有RDD。 虽然此方法更详细，但它用于在直到运行时才知道列及其类型时构造数据集。
         */

        // Create an RDD of Person objects from a text file
                JavaRDD<Person> peopleRDD = spark.read()
                        .textFile(textpath)
                        .javaRDD()
                        .map(line -> {
                            String[] parts = line.split(",");
                            Person person = new Person();
                            person.setName(parts[0]);
                            person.setAge(Integer.parseInt(parts[1].trim()));
                            return person;
                        });

         // Apply a schema to an RDD of JavaBeans to get a DataFrame
                Dataset<Row> peopleDF = spark.createDataFrame(peopleRDD, Person.class);
        // Register the DataFrame as a temporary view
                peopleDF.createOrReplaceTempView("people");

        // SQL statements can be run by using the sql methods provided by spark
                Dataset<Row> teenagersDF = spark.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19");

        // The columns of a row in the result can be accessed by field index
                Encoder<String> stringEncoder = Encoders.STRING();
                Dataset<String> teenagerNamesByIndexDF = teenagersDF.map(
                        (MapFunction<Row, String>) row -> "Name: " + row.getString(0),
                        stringEncoder);
                teenagerNamesByIndexDF.show();

        // or by field name
                Dataset<String> teenagerNamesByFieldDF = teenagersDF.map(
                        (MapFunction<Row, String>) row -> "Name: " + row.<String>getAs("name"),
                        stringEncoder);
                teenagerNamesByFieldDF.show();
        /**
         以编程方式指定Schema
         如果无法提前定义JavaBean类（例如，记录的结构以字符串形式编码，或者将解析文本数据集并且不同用户的字段将被不同地投影），则可以通过编程方式创建数据集<Row> 有三个步骤。

         从原始RDD创建行的RDD;
         创建由与步骤1中创建的RDD中的行结构匹配的StructType表示的模式。
         通过SparkSession提供的createDataFrame方法将模式应用于行的RDD。
         */

        // The schema is encoded in a string
          //String schemaString = "name age";

        // Create an RDD
        JavaRDD<String> peopleRDD1 = spark.sparkContext()
                .textFile(textpath, 1)
                .toJavaRDD();
        // Generate the schema based on the string of schema
        List<StructField> fields = new ArrayList<>();

        fields.add(DataTypes.createStructField("name", DataTypes.StringType, true));
        fields.add(DataTypes.createStructField("age", DataTypes.StringType, true));

                    StructType schema = DataTypes.createStructType(fields);

            // Convert records of the RDD (people) to Rows
                    JavaRDD<Row> rowRDD = peopleRDD1.map((Function<String, Row>) record -> {
                        String[] attributes = record.split(",");
                        return RowFactory.create(attributes[0], attributes[1].trim());
                    });

            // Apply the schema to the RDD
                    Dataset<Row> peopleDataFrame = spark.createDataFrame(rowRDD, schema);

            // Creates a temporary view using the DataFrame
                    peopleDataFrame.createOrReplaceTempView("people");

            // SQL can be run over a temporary view created using DataFrames
                    Dataset<Row> results = spark.sql("SELECT name FROM people");

            // The results of SQL queries are DataFrames and support all the normal RDD operations
            // The columns of a row in the result can be accessed by field index or by field name
                    Dataset<String> namesDS = results.map(
                            (MapFunction<Row, String>) row -> "Name: " + row.getString(0),
                            Encoders.STRING());
                    namesDS.show();
        /**
         聚合
         内置的DataFrames函数提供常见的聚合，如count（），countDistinct（），avg（），max（），min（）等。虽然这些函数是为DataFrames设计的，但Spark SQL也有类型安全的版本 其中一些在Scala和Java中使用强类型数据集。 此外，用户不限于预定义的聚合函数，并且可以创建自己的聚合函数。
         */
        /**
         part 2
         通用数据加载/保存功能
         手动指定选项
         直接在文件上运行SQL
         保存模式
         保存到持久表
         Bucketing，Sorting and Partitioning
         */
        /**
         读取和写入parquet文件
         */
        String bastpath= "/Users/wangleigis163.com/Documents/alex/dev/code/private/spark/src/main/resources/";
        String parquetpath= "/Users/wangleigis163.com/Documents/alex/dev/code/private/spark/src/main/resources/users.parquet";
        Dataset<Row> usersDF = spark.read().load(parquetpath);
        usersDF.select("name", "favorite_color").write().save(bastpath+System.currentTimeMillis()+"-namesAges.parquet");
        /**
         手动指定选项
         您还可以手动指定将要使用的数据源以及要传递给数据源的任何其他选项。 数据源由其完全限定名称（即org.apache.spark.sql.parquet）指定，但对于内置源，您还可以使用其短名称（json，parquet，jdbc，orc，libsvm，csv，text）。 从任何数据源类型加载的DataFrame都可以使用此语法转换为其他类型。
         要加载JSON文件，您可以使用：
         */


        Dataset<Row> peopleDFJson =
                spark.read().format("json").load(jsonpath);
        peopleDF.select("name", "age").write().format("parquet").save(bastpath+System.currentTimeMillis()+"-namesAges.parquet");
        /**
         在数据操作期间也使用其他更多选项。 例如，您可以控制ORC数据源的bloom过滤器和字典编码。
         以下ORC示例将在favorite_color上创建bloom过滤器，并对name和favorite_color使用字典编码。
         对于Parquet，也存在parquet.enable.dictionary。
         */
        Dataset<Row> peopleDFCsv = spark.read().format("csv")
                .option("sep", ";")//分割符
                .option("inferSchema", "true")
                .option("header", "true")
                .load(bastpath+"people.csv");
        peopleDFCsv.show();

//        usersDF.write().format("orc")
//                .option("orc.bloom.filter.columns", "favorite_color")
//                .option("orc.dictionary.key.threshold", "1.0")
//                .save("users_with_options.orc");

        /**
         直接在文件上运行SQL
         您可以直接使用SQL查询该文件，而不是使用读取API将文件加载到DataFrame并进行查询。
         */
        Dataset<Row> sqlOnFile =
                spark.sql("SELECT * FROM parquet.`"+bastpath+"users.parquet`");
        sqlOnFile.show();
        /**
         保存模式
         保存操作可以选择使用SaveMode，它指定如何处理现有数据（如果存在）。重要的是要意识到这些保存模式不使用任何锁定并且不是原子的。此外，执行覆盖时，将在写出新数据之前删除数据。

         Scala / Java任何语言含义
         SaveMode.ErrorIfExists（默认）“error”或“errorifexists”（默认）将DataFrame保存到数据源时，如果数据已存在，则会引发异常。
         SaveMode.Append“append”将DataFrame保存到数据源时，如果数据/表已经存在，则DataFrame的内容应该附加到现有数据。
         SaveMode.Overwrite“overwrite”覆盖模式意味着在将DataFrame保存到数据源时，如果数据/表已经存在，则预期现有数据将被DataFrame的内容覆盖。
         SaveMode.Ignore“忽略”忽略模式意味着在将DataFrame保存到数据源时，如果数据已经存在，则保存操作不会保存DataFrame的内容而不会更改现有数据。这类似于SQL中的CREATE TABLE IF NOT EXISTS。
         保存到持久表
         也可以使用saveAsTable命令将DataFrames作为持久表保存到Hive Metastore中。请注意，使用此功能不需要现有的Hive部署。 Spark将为您创建默认的本地Hive Metastore（使用Derby）。与createOrReplaceTempView命令不同，saveAsTable将实现DataFrame的内容并创建指向Hive Metastore中数据的指针。只要您保持与同一Metastore的连接，即使您的Spark程序重新启动后，持久表仍然存在。可以通过使用表的名称调用SparkSession上的table方法来创建持久表的DataFrame。

         对于基于文件的数据源，例如text，parquet，json等您可以通过路径选项指定自定义表路径，例如df.write.option（“path”，“/ some / path”）.saveAsTable（“t”）。删除表时，将不会删除自定义表路径，并且表数据仍然存在。如果未指定自定义表路径，则Spark会将数据写入仓库目录下的默认表路径。删除表时，也将删除默认表路径。

         从Spark 2.1开始，持久数据源表将每个分区元数据存储在Hive Metastore中。这带来了几个好处：

         由于Metastore只能返回查询所需的分区，因此不再需要在表的第一个查询中发现所有分区。
         现在，对于使用Datasource API创建的表，可以使用ALTER TABLE PARTITION ... SET LOCATION等Hive DDL。
         请注意，在创建外部数据源表（具有路径选项的表）时，默认情况下不会收集分区信息。要同步Metastore中的分区信息，可以调用MSCK REPAIR TABLE。
         */
        /**
         Bucketing，Sorting and Partitioning
         对于基于文件的数据源，还可以对输出进行存储和排序或分区。 分段和排序仅适用于持久表：
         */
        peopleDF.write().bucketBy(42, "name").sortBy("age").saveAsTable("people_bucketed");
        //虽然分区可以在使用数据集API时与save和saveAsTable一起使用。
         usersDF
                .write()
                .partitionBy("favorite_color")
                .format("parquet")
                .save("namesPartByColor.parquet");
       //可以对单个表使用分区和分桶
        peopleDF
                .write()
                .partitionBy("favorite_color")
                .bucketBy(42, "name")
                .saveAsTable("people_partitioned_bucketed");

        /**
         性能调优
         在内存中缓存数据
         其他配置选项
         SQL查询的广播提示
         对于某些工作负载，可以通过在内存中缓存数据或打开一些实验选项来提高性能。

         在内存中缓存数据
         Spark SQL可以通过调用spark.catalog.cacheTable（“tableName”）或dataFrame.cache（）使用内存中的列式格式来缓存表。然后，Spark SQL将仅扫描所需的列，并自动调整压缩以最小化内存使用和GC压力。您可以调用spark.catalog.uncacheTable（“tableName”）从内存中删除该表。

         可以使用SparkSession上的setConf方法或使用SQL运行SET key = value命令来完成内存中缓存的配置。

         属性名称默认含义
         spark.sql.inMemoryColumnarStorage.compressed true设置为true时，Spark SQL将根据数据统计信息自动为每列选择压缩编解码器。
         spark.sql.inMemoryColumnarStorage.batchSize 10000控制列式缓存的批次大小。较大的批处理大小可以提高内存利用率和压缩率，但在缓存数据时存在OOM风险。
         其他配置选项
         以下选项也可用于调整查询执行的性能。由于更多优化会自动执行，因此在将来的版本中可能会弃用这些选项。

         属性名称默认含义
         spark.sql.files.maxPartitionBytes 134217728（128 MB）读取文件时打包到单个分区的最大字节数。
         spark.sql.files.openCostInBytes 4194304（4 MB）打开文件的估计成本（以字节数为单位）可以同时扫描。将多个文件放入分区时使用。最好过度估计，然后使用较小文件的分区将比具有较大文件的分区（首先安排的分区）更快。
         spark.sql.broadcastTimeout 300
         广播连接中广播等待时间的超时（以秒为单位）

         spark.sql.autoBroadcastJoinThreshold 10485760（10 MB）配置在执行连接时将广播到所有工作节点的表的最大大小（以字节为单位）。通过将此值设置为-1，可以禁用广播。请注意，目前仅支持运行命令ANALYZE TABLE <tableName> COMPUTE STATISTICS noscan的Hive Metastore表的统计信息。
         spark.sql.shuffle.partitions 200配置在为连接或聚合洗牌数据时要使用的分区数。
         SQL查询的广播提示
         BROADCAST提示指导Spark在将其与另一个表或视图连接时广播每个指定的表。当Spark决定连接方法时，广播散列连接（即BHJ）是首选，即使统计信息高于配置spark.sql.autoBroadcastJoinThreshold。指定连接的两端时，Spark会广播具有较低统计信息的那一方。注意Spark并不保证始终选择BHJ，因为并非所有情况（例如全外连接）都支持BHJ。当选择广播嵌套循环连接时，我们仍然尊重提示。
         */

    }
}
