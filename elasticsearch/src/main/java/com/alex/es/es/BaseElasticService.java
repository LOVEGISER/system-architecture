package com.alex.es.es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Component
public class BaseElasticService {

    private static final Logger log = LoggerFactory.getLogger(BaseElasticService.class);

    @Autowired
    RestHighLevelClient restHighLevelClient;

  /**
   * @author alex @See
   * @date 2019/12/17 17:30
   * @param idxName 索引名称
   * @param idxDesc 索引描述
   * @return void
   * @throws
   * @since
   */
  public void createIndex(String idxName, int shards, int replicas, String idxDesc) {
    try {
      if (!this.indexExist(idxName)) {
        log.error(" idxName={} already exits,idxSql={}", idxName, idxDesc);
        return;
      }
      // 1：定义创建索引对象
      CreateIndexRequest request = new CreateIndexRequest(idxName);
      // 2：设置索引分片数量和副本数量
      request.settings(
          Settings.builder()
              .put("index.number_of_shards", shards)
              .put("index.number_of_replicas", replicas));
      // 3：设置索引mapping信息，这里设置索引描述
     // request.mapping(idxDesc, XContentType.JSON);
      CreateIndexResponse res =
          restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
      if (!res.isAcknowledged()) {
        throw new RuntimeException("createIndex acknowledged fail");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

    /** 断某个index是否存在
     * @author alex
     * @See
     * @date 2019/12/17 17:27
     * @param idxName index名
     * @return boolean
     * @throws
     * @since
     */
    public boolean indexExist(String idxName) throws Exception {
        // 1:定义查询索引对象
        GetIndexRequest request = new GetIndexRequest(idxName);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        //2：发起查询操作，返回结果
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    /** 断某个index是否存在
     * @author alex
     * @See
     * @date 2019/12/17 17:27
     * @param idxName index名
     * @return boolean
     * @throws
     * @since
     */
    public boolean isExistsIndex(String idxName) throws Exception {
        return restHighLevelClient.indices().exists(new GetIndexRequest(idxName),RequestOptions.DEFAULT);
    }

    /**
     * 查询索引信息
     * @param idxName
     * @return
     * @throws Exception
     */
    public GetIndexResponse getIndex(String idxName) throws Exception {
        // 1: 定义查询对象
        GetIndexRequest getIndexRequest = new GetIndexRequest(idxName);
        //2：执行索引查询操作，然后查询结果
        return  restHighLevelClient.indices().get(getIndexRequest,RequestOptions.DEFAULT);
    }
    /** 删除index
     * @author alex
     * @See
     * @date 2019/12/17 17:13
     * @param idxName
     * @return void
     * @throws
     * @since
     */
    public void deleteIndex(String idxName) {
        try {
            if (!this.indexExist(idxName)) {
                log.error(" idxName={} not exit",idxName);
                return;
            }
            AcknowledgedResponse  res = restHighLevelClient.indices().delete(new DeleteIndexRequest(idxName), RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("deleteIndex acknowledged fail");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * @author alex
     * @See
     * @date 2019/12/17 17:27
     * @param idxName index
     * @param document    对象
     * @return IndexResponse
     * @throws
     * @since
     */
    public IndexResponse add(String idxName, Document document) {
        //1：定义IndexRequest对象
        IndexRequest request = new IndexRequest(idxName);
        log.info("document : id={},document={}",document.getId(),JSON.toJSONString(document.getData()));
        //2：设置文档id
        request.id(document.getId());
        //3：设置文档数据
        request.source(document.getData(), XContentType.JSON);
        try {
            //4：执行创建索引操作
            IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            //5：返回查询结果
            return indexResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /** 批量插入数据
     * @author alex
     * @See
     * @date 2019/12/17 17:26
     * @param idxName index
     * @param list 插入数据列表
     * @return BulkResponse
     * @throws
     * @since
     */
    public BulkResponse batchAdd(String idxName, List<Document> list) {
        //1：定义BulkRequest对象
        BulkRequest request = new BulkRequest();
        //2：遍历list，将文档id和文档内容封装到BulkRequest中
        list.forEach(item ->
                request.add(new IndexRequest(idxName).id(item.getId())
                .source(item.getData(), XContentType.JSON)));
        try {
            //3：批量提交插入操作
            BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            //4：返回查询结果
            return bulkResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @author alex
     * @See
     * @date 2019/12/17 17:14
     * @param idxName index
     * @param builder   查询参数
     * @return java.util.List<T>
     * @throws
     * @since
     */
    public  List<String> search(String idxName, SearchSourceBuilder builder) {
        //1：定义SearchRequest对象
        SearchRequest request = new SearchRequest(idxName);
        //2：设置SearchSourceBuilder
        request.source(builder);
        try {
            //3：执行查询
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            //4：获取命中的结果
            SearchHit[] hits = response.getHits().getHits();
            //5：将命中的结果放入List
            List<String> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(hit.getSourceAsString());
            }
            //6：查询结果返回
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /** 批量删除
     * @author alex
     * @See
     * @date 2019/12/17 17:14
     * @param idxName index
     * @param idList    待删除列表
     * @return BulkResponse
     * @throws
     * @since
     */
    public <T> BulkResponse deleteBatch(String idxName, Collection<T> idList) {
        //1：定义BulkRequest对象
        BulkRequest request = new BulkRequest();
        //2：遍历list，将index和待删除文档id封装到BulkRequest中
        idList.forEach(item -> request.add(new DeleteRequest(idxName, item.toString())));
        try {
            //3：执行批量删除操作
            BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            return bulkResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @author alex
     * @See
     * @date 2019/12/17 17:13
     * @param idxName
     * @param builder
     * @return void
     * @throws
     * @since
     */
    public void deleteByQuery(String idxName, QueryBuilder builder) {

        DeleteByQueryRequest request = new DeleteByQueryRequest(idxName);
        request.setQuery(builder);
        //设置批量操作数量,最大为10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
