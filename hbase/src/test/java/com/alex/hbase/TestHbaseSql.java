
package com.alex.hbase;


import com.alibaba.fastjson.JSON;
import org.codehaus.jackson.schema.JsonSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 测试Hbase SQL
 *
 * @author alex
 * @date 2019/4/3
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TestHbaseSql {

    @Autowired
    private HBaseService hbaseService;
    private Logger log = LoggerFactory.getLogger(TestHbaseSql.class);

    /**
     * 测试删除、创建表
     */
    @Test
    public void test_() {
        String tableNanme = "test_persion_1";
        String cf = "cf";
        // step 1:创建表
       /*  hbaseService.creatTable(tableNanme, cf);
         //step 2:插入数据
        hbaseService.putData(tableNanme, "01", cf, new String[]{"id", "name"}, new String[]{"01", "张三"});
        hbaseService.putData(tableNanme, "02", cf, new String[]{"id", "name"}, new String[]{"02", "李四"});
        hbaseService.putData(tableNanme, "03", cf, new String[]{"id", "name"}, new String[]{"03", "王五"});*/
        //step 3: 根据rowKey查询
      //  while (true){
            long start = System.currentTimeMillis();
            Map<String, String> result = hbaseService.get(tableNanme, cf, "01");
            long end = System.currentTimeMillis();
            log.info("time:["+(end-start)+"]"+JSON.toJSONString(result));
       // }


         // step4: 根据RowKey范围查询
//        Map<String, Map<String, String>> result2 = hbaseService.getResultScanner(tableNanme, "01", "03");
//        result2.forEach((k, value) -> {
//            log.info(k + ":" + value);
//        });
    }

}
