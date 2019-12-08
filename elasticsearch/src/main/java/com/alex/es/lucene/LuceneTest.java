package com.alex.es.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

public class LuceneTest {

    public static void main(String[] args) throws IOException {
        LuceneTest luceneTest = new LuceneTest();
        luceneTest.createIndex();
    }
    public void createIndex() throws IOException {
        Article article = new Article();
//      即使重复也可以
        article.setId(108L);
        article.setAuthor("王磊");
        article.setTitle("offer来了之Lucene学习");
        article.setContent("this is a simple lucene demo");
        article.setUrl("http://www.edu360.cn/a10011");
//        指定目录 数据写入目录
        String indexPath = "/Users/wangleigis163.com/Documents/alex/dev/code/private/system-architecture/elasticsearch/src/main/resources/lucene/index";
        FSDirectory fsDirectory = FSDirectory.open(Paths.get(indexPath));
        //创建一个标准分词器，一个字分一次 无法分中文 例如：“老师”会分成“老”“师”
        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer(true);
        //写入索引的配置，设置了分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        //指定了写入数据目录和配置
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);
        //创建一个文档对象
        Document document = article.toDocument();
        //通过IndexWriter写入
        indexWriter.addDocument(document);
        indexWriter.close();
    }

}
