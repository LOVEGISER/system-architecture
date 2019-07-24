package rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * mvn package

 cd /Users/wangleigis163.com/Documents/alex/dev/evn/spark-2.4.3-bin-hadoop2.7/
 ./bin/spark-submit  --class "rdd.RDDSimple" --master local[4] /Users/wangleigis163.com/Documents/alex/dev/code/private/spark/target/spark-1.0.jar

 */

public class RDDSimple {
    public static void main(String[] args) {
        //1:初始化JavaSparkContext
        SparkConf conf = new SparkConf().setAppName(RDDSimple.class.getName()).setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        String filepath = "/Users/wangleigis163.com/Documents/alex/dev/code/private/spark/src/main/resources/temp.txt";
        /*2:根据集合生成RDD:根据JavaSparkContext的parallelize方法将已经存在的一个集合转换为RDD，
           集合中的数据将被复制到RDD(分布式数据集)并参与并行计算

           并行集合的一个重要参数是将数据集切割为的分区数。 Spark将为群集的每个分区运行一个任务。 通常，您希望群集中的每个CPU有2-4个分区。 通常，Spark会尝试根据您的群集自动设置分区数。 但是，您也可以通过将其作为第二个参数传递给并行化来手动设置它（例如sc.parallelize（data，10））。

        */
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> distData = sc.parallelize(data);
        long sum = distData.reduce((a, b) -> a + b);
        System.out.println("[ Collection to RDD ] sum is:"+sum);
        /**
         * 3：从外部数据集生成RDD:
         *Spark可以从Hadoop支持的任何存储源创建分布式数据集，包括本地文件系统，HDFS，Cassandra，HBase，Amazon S3等.Spark支持文本文件，SequenceFiles和任何其他Hadoop InputFormat。
         * 可以使用SparkContext的textFile方法创建文本文件RDD。 此方法获取文件的URI（计算机上的本地路径，或hdfs：//，s3a：//等URI）并将其作为行集合读取。

         有关使用Spark读取文件的一些注意事项：
                 1：如果在本地文件系统上使用路径，则还必须可以在工作节点上的相同路径上访问该文件。一般做法是将文件复制到所有工作者或使用网络安装的共享文件系统。
                 2：Spark的所有基于文件的输入方法（包括textFile）都支持在目录、压缩文件和通配符上运行。例如：
                   您可以使用textFile（“/my /directory”）表示一个路径，spark会加载路径下的所有文件；
                    textFile（“/my/directory/*.txt”）表示加载路径下所有以.txt为后缀名的文件
                    textFile（“/my/directory/*.gz”）表示加载并解压所有以.gz为后缀名的文件
                3：textFile方法还采用可选的第二个参数来控制文件的分区数。默认情况下，Spark为文件的每个块创建一个分区（HDFS中默认为128MB），但您也可以通过传递更大的值来请求更多的分区。请注意，您不能拥有比块少的分区。
                4：除文本文件外，Spark的Java API还支持其他几种数据格式：
                  a:JavaSparkContext.wholeTextFiles允许您读取包含多个小文本文件的目录，并将每个文件作为（文件名，内容）对返回。这与textFile形成对比，textFile将在每个文件中每行返回一条记录。
                  b:对于SequenceFiles，使用SparkContext的sequenceFile [K，V]方法，其中K和V是文件中键和值的类型。这些应该是Hadoop的Writable接口的子类，如IntWritable和Text。
                  c:对于其他Hadoop InputFormats，您可以使用JavaSparkContext.hadoopRDD方法，该方法采用任意JobConf和输入格式类，键类和值类。设置这些与使用输入源的Hadoop作业的方式相同。您还可以使用基于“新”MapReduce API（org.apache.hadoop.mapreduce）的InputFormats的JavaSparkContext.newAPIHadoopRDD。
                  d:JavaRDD.saveAsObjectFile和JavaSparkContext.objectFile支持以包含序列化Java对象的简单格式保存RDD。虽然这不像Avro这样的专用格式有效，但它提供了一种保存任何RDD的简便方法。
         */
        JavaRDD<String> rddFormFlie = sc.textFile("/Users/wangleigis163.com/Documents/alex/dev/code/private/spark/src/main/resources/temp.txt");
        long count = rddFormFlie.map(s -> s.length()).reduce((a, b) -> a + b);
        System.out.println("[ File to RDD ] count is:"+count);
        /**
         * RDD操作
         * RDD支持两种类型的操作：转换（从现有数据集创建新数据集）和操作（在数据集上运行计算后将值返回到驱动程序）。例如，map是一个转换，它通过一个函数传递每个数据集元素，并返回一个表示结果的新RDD。另一方面，reduce是一个使用某个函数聚合RDD的所有元素的操作，并将最终结果返回给驱动程序（尽管还有一个返回分布式数据集的并行reduceByKey）。
         * Spark中的所有转换都是惰性的，因为它们不会立即计算结果。相反，他们只记得应用于某些基础数据集（例如文件）的转换。仅当操作需要将结果返回到驱动程序时才会计算转换。这种设计使Spark能够更有效地运行。例如，我们可以意识到通过map创建的数据集将用于reduce，并且仅将reduce的结果返回给驱动程序，而不是更大的映射数据集。
         * 默认情况下，每次对其执行操作时，都可以重新计算每个转换后的RDD。但是，您也可以使用持久化（或缓存）方法在内存中保留RDD，在这种情况下，Spark会在群集上保留元素，以便在下次查询时更快地访问。还支持在磁盘上保留RDD或在多个节点上复制。
        */
        JavaRDD<String> lines = sc.textFile(filepath);
        JavaRDD<Integer> lineLengths = lines.map(s -> s.length());
        int totalLength = lineLengths.reduce((a, b) -> a + b);
        System.out.println("[ spark map reduce operation： ] count is:"+totalLength);
        /**
         sc.textFile()定义来自外部文件的基本RDD，此数据集未加载到内存中或以其他方式执行：行仅仅是指向文件的指针。
         lines.map将lineLengths定义为map转换。同样，由于懒惰，lineLengths不会立即计算。最终，我们运行类似reduce个动作时Spark将计算分解为在不同机器上运行的任务，
         每台机器都运行其部分map数据和并将运行结果保存为本地的reduce，当节点运算完成或将reduce结果返回给driver程度进行结果合并。
         如果我们以后想再次使用lineLengths，我们可以添加：lineLengths.persist(StorageLevel.MEMORY_ONLY());
         这样在reduce之前，lineLengths在第一次map计算结构保存在内存中，方便直接使用
         */

        /**
         * 将函数传递给Spark:
         *
         Spark的API依赖于在driver程序中传的递函数以完成在集群上运行和对数据的计算。
         在Java中，函数由实现org.apache.spark.api.java.function包中的接口的类表示。 有两种方法可以创建这样的函数：
         1：在您自己的类中实现Function接口，可以是匿名内部类，也可以是命名接口，并将其实例传递给Spark。
         2：使用lambda表达式简明地定义实现。
         虽然lambda语法来简洁，方便使用。但在复杂的应用中我们还是需要定义自己的function，例如我们可以按如下方式编写自定义function：
         *
         */
        //直接定义function
        JavaRDD<String> lines1 =sc.textFile(filepath);
        //Function是基本的操作，表示参数是一个数据的函数
        JavaRDD<Integer> lineLengths1 = lines1.map(new Function<String, Integer>() {
            public Integer call(String s) { return s.length(); }
        });
        //Function2表示2个参数的函数操作
        int totalLength1 = lineLengths1.reduce(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer a, Integer b) { return a + b; }
        });
        System.out.println("[ spark default function operation： ] count is:"+totalLength1);


        //通过类的形式定义function
        class GetLength implements Function<String, Integer> {
            public Integer call(String s) { return s.length(); }
        }
        class Sum implements Function2<Integer, Integer, Integer> {
            public Integer call(Integer a, Integer b) { return a + b; }
        }
        JavaRDD<String> lines2 = sc.textFile(filepath);
        JavaRDD<Integer> lineLengths2 = lines2.map(new GetLength());
        int totalLength2 = lineLengths2.reduce(new Sum());
        System.out.println("[ spark class function operation： ] count is:"+totalLength2);

        /**
         * 请注意，Java中的匿名内部类也可以访问当前类范围中的变量，只要它们标记为final即可。 Spark会将这些变量的副本发送到每个工作节点，
         */


        /**
         * 使用键值对
         * 虽然大多数Spark操作都适用于包含任何类型对象的RDD,
         * 但一些特殊操作仅用于键值对的RDD。 最常见的是分布式“shuffle”操作通过key对元素进行分组或聚合。
         *
         * 在Java中，键值对使用Scala标准库中的scala.Tuple2类表示。 您可以简单地调用new Tuple2（a，b）来创建元组，然后使用tuple._1（）和tuple._2（）访问其第一个字段和第二个字段。
         *
         * 键值对的RDD由JavaPairRDD类表示。 您可以使用特殊的map操作（例如mapToPair和flatMapToPair）从JavaRDDs构建JavaPairRDD。 JavaPairRDD将具有标准RDD功能和特殊键值功能。
         *
         * 例如，以下代码对键值对使用reduceByKey操作来计算文件中每行文本出现的次数：
         */
        JavaRDD<String> lines3 = sc.textFile(filepath);
        JavaPairRDD<String, Integer> pairs3 = lines3.mapToPair(s -> new Tuple2(s, 1));
        JavaPairRDD<String, Integer> counts3 = pairs3.reduceByKey((a, b) -> a + b);
        System.out.println("[ spark JavaPairRDD operation： ] count is:"+counts3);
        /**
         * 例如，我们也可以使用counts.sortByKey（）来按字母顺序对这些对进行排序，最后使用counts.collect（）将它们作为对象数组返回到驱动程序。
         * 注意：使用自定义对象作为键值对操作中的键时，必须确保覆写对象的equals（）方法和hashCode（）方法。*/

        /**
         * Transformations list
         * 转型意义
         * map（func）返回通过函数func传递源的每个元素形成的新分布式数据集。
         * filter（func）返回通过选择func返回true的源元素形成的新数据集。
         * flatMap（func）与map类似，但每个输入项可以映射到0个或更多输出项（因此func应返回Seq而不是单个项）。
         * mapPartitions（func）与map类似，但在RDD的每个分区（块）上单独运行，因此当在类型T的RDD上运行时，func必须是Iterator <T> => Iterator <U>类型。
         * mapPartitionsWithIndex（func）与mapPartitions类似，但也为func提供了一个表示分区索引的整数值，因此当在RDD类型上运行时，func必须是类型（Int，Iterator <T>）=> Iterator <U> T.
         * sample（withReplacement，fraction，seed）使用给定的随机数生成器种子，使用或不使用替换对数据的一小部分进行采样。
         * union（otherDataset）返回一个新数据集，其中包含源数据集和参数中元素的并集。
         * intersection（otherDataset）返回包含源数据集和参数中元素交集的新RDD。
         * distinct（[numPartitions]））返回包含源数据集的不同元素的新数据集。
         * groupByKey（[numPartitions]）在（K，V）对的数据集上调用时，返回（K，Iterable <V>）对的数据集。
         * 注意：如果要对每个键执行聚合（例如总和或平均值）进行分组，则使用reduceByKey或aggregateByKey将产生更好的性能。
         * 注意：默认情况下，输出中的并行级别取决于父RDD的分区数。您可以传递可选的numPartitions参数来设置不同数量的任务。
         * reduceByKey（func，[numPartitions]）当在（K，V）对的数据集上调用时，返回（K，V）对的数据集，其中使用给定的reduce函数func聚合每个键的值，该函数必须是type（V，V）=> V.与groupByKey类似，reduce任务的数量可通过可选的第二个参数进行配置。
         * aggregateByKey（zeroValue）（seqOp，combOp，[numPartitions]）在（K，V）对的数据集上调用时，返回（K，U）对的数据集，其中使用给定的组合函数聚合每个键的值，中性的“零”值。允许与输入值类型不同的聚合值类型，同时避免不必要的分配。与groupByKey类似，reduce任务的数量可通过可选的第二个参数进行配置。
         * sortByKey（[ascending]，[numPartitions]）在K实现Ordered的（K，V）对的数据集上调用时，返回按键按升序或降序排序的（K，V）对数据集，如布尔升序参数。
         * join（otherDataset，[numPartitions]）当调用类型为（K，V）和（K，W）的数据集时，返回（K，（V，W））对的数据集以及每个键的所有元素对。通过leftOuterJoin，rightOuterJoin和fullOuterJoin支持外连接。
         * cogroup（otherDataset，[numPartitions]）当调用类型为（K，V）和（K，W）的数据集时，返回（K，（Iterable <V>，Iterable <W>））元组的数据集。此操作也称为groupWith。
         * cartesian（otherDataset）当调用类型为T和U的数据集时，返回（T，U）对的数据集（所有元素对）。
         * pipe（command，[envVars]）通过shell命令管道RDD的每个分区，例如：一个Perl或bash脚本。 RDD元素被写入进程的stdin，并且输出到其stdout的行将作为字符串的RDD返回。
         * coalesce（numPartitions）将RDD中的分区数减少为numPartitions。过滤大型数据集后，可以更有效地运行操作。
         * repartition（numPartitions）随机重新调整RDD中的数据以创建更多或更少的分区并在它们之间进行平衡。这总是随机播放网络上的所有数据。
         * repartitionAndSortWithinPartitions（partitioner）根据给定的分区程序重新分区RDD，并在每个生成的分区中按键对记录进行排序。这比调用重新分区然后在每个分区内排序更有效，因为它可以将排序推送到shuffle机器中。
         *
         */
        /**
         *

         行动意义
         reduce（func）使用函数func（它接受两个参数并返回一个）来聚合数据集的元素。该函数应该是可交换的和关联的，以便可以并行正确计算。
         collect（）在驱动程序中将数据集的所有元素作为数组返回。在过滤器或其他返回足够小的数据子集的操作之后，这通常很有用。
         count（）返回数据集中的元素数。
         first（）返回数据集的第一个元素（类似于take（1））。
         take（n）返回包含数据集的前n个元素的数组。
         takeSample（withReplacement，num，[seed]）返回一个数组，其中包含数据集的num个元素的随机样本，有或没有替换，可选地预先指定随机数生成器种子。
         takeOrdered（n，[ordering]）使用自然顺序或自定义比较器返回RDD的前n个元素。
         saveAsTextFile（path）将数据集的元素写为本地文件系统，HDFS或任何其他Hadoop支持的文件系统中给定目录中的文本文件（或文本文件集）。 Spark将在每个元素上调用toString，将其转换为文件中的一行文本。
         saveAsSequenceFile（路径）
         （Java和Scala）将数据集的元素作为Hadoop SequenceFile写入本地文件系统，HDFS或任何其他Hadoop支持的文件系统中的给定路径中。这可以在实现Hadoop的Writable接口的键值对的RDD上使用。在Scala中，它也可以在可隐式转换为Writable的类型上使用（Spark包括基本类型的转换，如Int，Double，String等）。
         saveAsObjectFile（路径）
         （Java和Scala）使用Java序列化以简单格式编写数据集的元素，然后可以使用SparkContext.objectFile（）加载它。
         countByKey（）仅适用于类型为（K，V）的RDD。返回（K，Int）对的散列映射，其中包含每个键的计数。
         foreach（func）对数据集的每个元素运行函数func。这通常用于副作用，例如更新累加器或与外部存储系统交互。
         注意：在foreach（）之外修改除累加器之外的变量可能会导致未定义的行为。

         */
        /**
         * 随机操作
         * Spark中的某些操作会触发称为shuffle的事件。shuffle是Spark的重新分配数据的机制，因此它可以跨分区进行不同的分组。
         *
         * 背景
         * 为了理解在shuffle期间发生的事情，我们可以考虑reduceByKey操作的示例。 reduceByKey操作生成一个新的RDD，其中单个键的所有值都组合成一个元组 - 键和对与该键关联的所有值执行reduce函数的结果。挑战在于，没单个key的所有值都位于不同一个分区，甚至是不同的机器上，但它们必须位于同一位置才能计算结果。
         *
         * 在Spark中，数据通常不跨分区分布，以便在特定操作的必要位置。在计算过程中，单个任务将在单个分区上运行 - 因此，要组织单个reduceByKey reduce任务执行的所有数据，Spark需要执行全部操作。它必须从所有分区读取以查找所有键的所有值，然后将分区中的值汇总在一起以计算每个键的最终结果 - 这称为shuffle。
         *
         * 尽管新洗牌数据的每个分区中的元素集将是确定性的，并且分区本身的排序也是如此，但这些元素的排序不是。如果在随机播放后需要可预测的有序数据，则可以使用：
         *
         * mapPartitions使用例如.sorted对每个分区进行排序
         * repartitionAndSortWithinPartitions在同时重新分区的同时有效地对分区进行排序
         * sortBy来创建一个全局排序的RDD
         * 可以导致混洗的操作包括重新分区操作，如重新分区和合并，“ByKey操作（计数除外），如groupByKey和reduceByKey，以及联合操作，如cogroup和join。

         绩效影响
         Shuffle是一项昂贵的操作，因为它涉及磁盘I / O，数据序列化和网络I / O.为了组织shuffle的数据，Spark生成了一系列任务 - 映射任务以组织数据，以及一组reduce任务来聚合它。这个术语来自MapReduce，并不直接与Spark的地图和减少操作相关。

         在内部，各个地图任务的结果会保留在内存中，直到它们无法适应。然后，这些基于目标分区进行排序并写入单个文件。在reduce方面，任务读取相关的排序块。

         某些shuffle操作会消耗大量的堆内存，因为它们使用内存中的数据结构来在传输记录之前或之后组织记录。具体来说，reduceByKey和aggregateByKey在地图侧创建这些结构，并且'ByKey操作在reduce侧生成这些结构。当数据不适合内存时，Spark会将这些表溢出到磁盘，从而导致磁盘I / O的额外开销和垃圾收集增加。

         Shuffle还会在磁盘上生成大量中间文件。从Spark 1.3开始，这些文件将被保留，直到不再使用相应的RDD并进行垃圾回收。这样做是为了在重新计算谱系时不需要重新创建shuffle文件。如果应用程序保留对这些RDD的引用或GC不经常启动，则垃圾收集可能仅在很长一段时间后才会发生。这意味着长时间运行的Spark作业可能会占用大量磁盘空间。配置Spark上下文时，spark.local.dir配置参数指定临时存储目录。

         可以通过调整各种配置参数来调整随机行为。请参阅“Spark配置指南”中的“随机行为”部分。
         */

        /**
         * RDD持久性
         * Spark中最重要的功能之一是跨操作在内存中持久化（或缓存）数据集。当您持久保存RDD时，每个节点都会存储它在内存中计算的任何分区，并在该数据集（或从中派生的数据集）的其他操作中重用它们。这使得未来的行动更快（通常超过10倍）。缓存是迭代算法和快速交互式使用的关键工具。
         *
         * 您可以使用persist（）或cache（）方法标记要保留的RDD。第一次在动作中计算它，它将保留在节点的内存中。 Spark的缓存是容错的 - 如果丢失了RDD的任何分区，它将使用最初创建它的转换自动重新计算。
         *
         * 此外，每个持久化RDD可以使用不同的存储级别进行存储，例如，允许您将数据集保留在磁盘上，将其保留在内存中，但作为序列化Java对象（以节省空间），跨节点复制它。通过将StorageLevel对象（Scala，Java，Python）传递给persist（）来设置这些级别。 cache（）方法是使用默认存储级别的简写，即StorageLevel.MEMORY_ONLY（在内存中存储反序列化的对象）。完整的存储级别是：
         *
         * 存储级别含义
         * MEMORY_ONLY将RDD存储为JVM中的反序列化Java对象。如果RDD不适合内存，则某些分区将不会被缓存，并且每次需要时都会重新计算。这是默认级别。
         * MEMORY_AND_DISK将RDD存储为JVM中的反序列化Java对象。如果RDD不适合内存，请存储不适合磁盘的分区，并在需要时从那里读取它们。
         * MEMORY_ONLY_SER
         * （Java和Scala）将RDD存储为序列化Java对象（每个分区一个字节数组）。这通常比反序列化对象更节省空间，特别是在使用快速序列化器时，但读取CPU密集程度更高。
         * MEMORY_AND_DISK_SER
         * （Java和Scala）与MEMORY_ONLY_SER类似，但是将不适合内存的分区溢出到磁盘，而不是每次需要时动态重新计算它们。
         * DISK_ONLY仅将RDD分区存储在磁盘上。
         * MEMORY_ONLY_2，MEMORY_AND_DISK_2等。与上面的级别相同，但复制两个群集节点上的每个分区。
         * OFF_HEAP（实验）与MEMORY_ONLY_SER类似，但将数据存储在堆外内存中。这需要启用堆外内存。

         选择哪种存储级别？
         Spark的存储级别旨在提供内存使用和CPU效率之间的不同折衷。我们建议您通过以下流程选择一个：

         如果您的RDD与默认存储级别（MEMORY_ONLY）很舒适，请保持这种状态。这是CPU效率最高的选项，允许RDD上的操作尽可能快地运行。

         如果没有，请尝试使用MEMORY_ONLY_SER并选择快速序列化库，以使对象更节省空间，但仍然可以快速访问。 （Java和Scala）

         除非计算数据集的函数很昂贵，否则它们不会溢出到磁盘，或者它们会过滤大量数据。否则，重新计算分区可能与从磁盘读取分区一样快。

         如果要快速故障恢复，请使用复制的存储级别（例如，如果使用Spark来处理来自Web应用程序的请求）。所有存储级别通过重新计算丢失的数据提供完全容错，但复制的存储级别允许您继续在RDD上运行任务，而无需等待重新计算丢失的分区。

         删除数据
         Spark会自动监视每个节点上的缓存使用情况，并以最近最少使用（LRU）的方式删除旧数据分区。如果您想手动删除RDD而不是等待它退出缓存，请使用RDD.unpersist（）方法。

         */
        /**

         广播变量
         广播变量允许程序员在每台机器上保留一个只读变量，而不是随副本一起发送它的副本。例如，它们可用于以有效的方式为每个节点提供大输入数据集的副本。 Spark还尝试使用有效的广播算法来分发广播变量，以降低通信成本。

         Spark动作通过一组阶段执行，由分布式“shuffle”操作分隔。 Spark自动广播每个阶段中任务所需的公共数据。以这种方式广播的数据以序列化形式缓存并在运行每个任务之前反序列化。这意味着显式创建广播变量仅在跨多个阶段的任务需要相同数据或以反序列化形式缓存数据很重要时才有用。

         通过调用SparkContext.broadcast（v）从变量v创建广播变量。广播变量是v的包装器，可以通过调用value方法访问其值。下面的代码显示了这个：

         */
        Broadcast<int[]> broadcastVar = sc.broadcast(new int[] {1, 2, 3});
        broadcastVar.value();
//      returns [1, 2, 3]


    }

}
