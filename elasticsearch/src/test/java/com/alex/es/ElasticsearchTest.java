package com.alex.es;

import com.alex.es.es.BaseElasticService;
import com.alex.es.es.Document;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchTest {

  @Autowired public BaseElasticService baseElasticService;
  private static final Logger log = LoggerFactory.getLogger(BaseElasticService.class);
  final String indexName = "index-1";

  @Test
  public void IndexAPITest() {

    try {
      // 1:索引新建，查询和删除

        baseElasticService.createIndex(indexName, 3, 2, indexName);
//        System.out.println("===========getIndex=====================");
//        System.out.println(JSONObject.toJSONString(baseElasticService.getIndex(indexName).getMappings()));
      baseElasticService.getIndex(indexName);
        baseElasticService.deleteIndex(indexName);



    } catch (Exception e) {
      e.printStackTrace();
    }
  }


    @Test
    public void DocumentAPITest() {

        try {
       //1:索引新建
        baseElasticService.createIndex(indexName, 3, 2, indexName);
      // 2: 插入文档
                  Map map = new HashMap();
                  map.put("name", "alex");
                  map.put("age", 30);
                  map.put("location", "beijing");

                  Document document = new Document("1", JSONObject.toJSONString(map));
                  System.out.println("===========add=====================");
                  System.out.println(JSONObject.toJSONString(baseElasticService.add(indexName,
       document)));

      List<Document> docList = new ArrayList<Document>();
      for (int i = 0; i < 10; i++) {
        Map mapq = new HashMap();
        map.put("name", "alex");
        map.put("age", 30);
        map.put("location", "beijing");
        map.put("number", i);
        Document doc = new Document(String.valueOf(i), JSONObject.toJSONString(map));
        docList.add(doc);
      }
      baseElasticService.batchAdd(indexName,docList);
      // 3: 查询文档
      SearchSourceBuilder builder = new SearchSourceBuilder();

      // builder.query(QueryBuilders.matchAllQuery()); // 添加 match_all 查询

      builder.query(QueryBuilders.termQuery("name", "alex")); // 设置搜索，可以是任何类型的 QueryBuilder
      builder.from(0); // 起始 index
      builder.size(2); // 大小 size
      builder.timeout(new TimeValue(60, TimeUnit.SECONDS)); // 设置搜索的超时时间

      List list = baseElasticService.search(indexName, builder);
      Thread.sleep(1000);
      System.out.println("===========search=====================");
      System.out.println(JSONObject.toJSONString(list));

             // 4：删除文档
//            List idList = new ArrayList<>();
//            idList.add("1");
//            baseElasticService.deleteBatch(indexName,idList);
//            System.out.println("===========deleteBatch=====================");
//            List  list1 = baseElasticService.search(indexName,builder);
//            System.out.println("===========search=====================");
//            System.out.println(JSONObject.toJSONString(list1));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
