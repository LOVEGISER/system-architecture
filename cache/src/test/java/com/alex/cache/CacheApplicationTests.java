package com.alex.cache;

import com.alex.cache.dao.User;
import com.alex.cache.dao.UserRepository;
import com.alex.cache.ehcache.UserService;
import com.alex.cache.memcached.ShowApi;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(CacheApplicationTests.class);
    @Autowired
    private ShowApi showApi;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Test
    public void contextLoads() {

//        String key = "goods_name";
//        String value = "apple";
//        showApi.showAdd(key, value);
//        System.out.println(showApi.showQuery(key));

    }

    @Test
    public void jpa() {
        User user = new User();
        user.setEmail("alex@163.com");
        user.setName("alex");
        user.setId(UUID.randomUUID().toString());
        User userAdd = userRepository.save(user);
        Assert.assertNotNull(userAdd);
        logger.info("add result:"+JSON.toJSONString(userAdd));
        User userSearch = userRepository.getOne(userAdd.getId());
        Assert.assertNotNull(userSearch);

    }
    @Test
    public void ehcache() {
        User user = new User();
        user.setEmail("alex@163.com");
        user.setName("alex");
        user.setId(UUID.randomUUID().toString());
        User userAdd = userService.save(user);
        Assert.assertNotNull(userAdd);
        User userSearch = userService.findOne(userAdd.getId());
        Assert.assertNotNull(userSearch);
        userService.findOne(userAdd.getId());
        userService.findOne(userAdd.getId());

    }



}
