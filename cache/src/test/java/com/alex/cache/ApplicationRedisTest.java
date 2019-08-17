package com.alex.cache;

import com.alex.cache.dao.User;
import com.alex.cache.dao.UserRepository;
import com.alex.cache.ehcache.UserService;
import com.alex.cache.memcached.ShowApi;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationRedisTest {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationRedisTest.class);
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        // 1:Redis key-value插入
        redisTemplate.opsForValue().set("key","value");
         // 2:Redis 根据key查询
        Object result = redisTemplate.opsForValue().get("key");
        // 3:Redis 根据key删除
        redisTemplate.delete("key");
        //4：Redis Pipeline 执行批量操作，造作结果返回在list中
        List<Object> list = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Nullable
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();//1：打开Pipeline
                for (int i = 0; i < 10000; i++) {//2:执行批量操作
                    String key = "key_" + i;
                    String value = "value_" + i;
                    connection.set(key.getBytes(),value.getBytes());
                }
                return null;
            }

        });
        //5：查看管道批量操作返回结果
        for (Object item: list) {
           System.out.println(item);
        }






    }

    public void transactionSet(Map<String,Object> commandList){
        //1：开启事务权限
        redisTemplate.setEnableTransactionSupport(true);
        try {
            //2：开启事务
            redisTemplate.multi();
            //3：执行事务命令
            for(Map.Entry<String, Object> entry : commandList.entrySet()){
                String mapKey = entry.getKey();
                Object mapValue = entry.getValue();
                redisTemplate.opsForValue().set(mapKey, mapValue);
            }
            //成功就提交
            redisTemplate.exec();
        } catch (Exception e) {
            //失败了就回滚
            redisTemplate.discard();
        }

    }
}
