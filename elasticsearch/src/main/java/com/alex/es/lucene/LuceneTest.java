package com.alex.es.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;


import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;

public class LuceneTest {
 final String   indexPath = "/Users/wangleigis163.com/Documents/alex/dev/code/private/system-architecture/elasticsearch/src/main/resources/lucene/index";
    public static void main(String[] args) throws Exception {
        LuceneTest luceneTest = new LuceneTest();
        luceneTest.createIndex();
        luceneTest.searchIndex();
    }
    public void createIndex() throws IOException {
        //1：原始数据创建
        Article article = new Article();
        article.setId(108L);
        article.setAuthor("王磊");
        article.setTitle("offer来了之Lucene学习");
        article.setContent("this is a simple lucene demo");
        article.setUrl("https://github.com/LOVEGISER/system-architecture/tree/master/elasticsearch");
       //2：指定倒排索引写入的目录
        FSDirectory fsDirectory = FSDirectory.open(Paths.get(indexPath));
        //3：创建一个标准分词器，一个字分一次 无法分中文 例如：“王磊”会分成“王”“磊”
        Analyzer analyzer = new StandardAnalyzer();
        //3：创建分词器
       // Analyzer analyzer = new IKAnalyzer(true);
        //4:定义写入索引的配置信息设置：设置了分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        //5：定义IndexWriter用于索引写入：指定了写入数据目录和配置
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);
        //6：创建一个文档对象
        Document document = articleToDocument(article);
        //7：通过IndexWriter写入索引
        indexWriter.addDocument(document);
        indexWriter.close();
    }

    public void searchIndex() throws Exception {


       // 1：定义查询分词器：读跟写要用相同的分词器
        Analyzer analyzer = new StandardAnalyzer();
        //Analyzer analyzer = new IKAnalyzer(true);
        DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        //2：定义索引查询器
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
       //3：定义要查找的关键字
        String queryStr = "lucene";
        //4：创建一个查询条件解析器 "content"表示从content中查找
        QueryParser parser = new QueryParser("content", analyzer);
        //5：对查询条件进行解析
        Query query = parser.parse(queryStr);
        //TermQuery将查询条件当成是一个固定的词
        //Query query = new TermQuery(new Term("url", "http://www.edu360.cn/a10010"));
        //在【索引库】中进行查找 10 表示查找前10个
        TopDocs topDocs = indexSearcher.search(query, 10);
        //6：获取到查找到的文文档ID和得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            //从索引中查询到文档的ID，跟我们自定的实体类id没有关系
            int doc = scoreDoc.doc;
            //在根据ID到文档中查找文档内容
            Document document = indexSearcher.doc(doc);
            //将文档转换成对应的实体类
            Article article = documentToArticle(document);
            System.out.println(article);
        }
      //7：释放资源
        directoryReader.close();
    }


    public Document articleToDocument(Article article){
        //Lucene存储的格式
        Document doc = new Document();
        //向文档中添加一个long类型的属性，建立索引
        doc.add(new LongPoint("id", article.getId()));
        //在文档中存储
        doc.add(new StoredField("id", article.getId()));
        //设置一个文本类型，会对内容进行分词，建立索引，并将内容在文档中存储
        doc.add(new TextField("title", article.getTitle(), Field.Store.YES));
        //设置一个文本类型，会对内容进行分词，建立索引，存在文档中存储 / No代表不存储
        //Store.No只是不在文档中存储
        doc.add(new TextField("content", article.getContent(), Field.Store.YES));
        //StringField，不分词，建立索引，文档中存储，因为不分词，所以查询时要输入全内容
        doc.add(new StringField("author", article.getAuthor(), Field.Store.YES));
        //不分词，不建立索引，在文档中存储，
        doc.add(new StoredField("url", article.getUrl()));
        return doc;
    }
    public static Article documentToArticle(Document doc){
        Long id = Long.parseLong(doc.get("id"));
        String title = doc.get("title");
        String content = doc.get("content");
        String author = doc.get("author");
        String url = doc.get("url");
        Article article = new Article(id, title, content, author, url);
        return article;
    }

}
