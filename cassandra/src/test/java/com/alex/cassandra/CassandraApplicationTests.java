package com.alex.cassandra;

import com.alex.cassandra.user.User;
import com.alex.cassandra.user.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CassandraApplicationTests {

    @Autowired
    UserDao userDao;
    @Test
    public void SQLTest() {

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUserName("alex");
        //step 1 : add data
        userDao.inert(user);
        System.out.println("insert:"+userDao.selectOne(user).toString());
        user.setUserName("vic");
        //step 2 : update data
        userDao.inert(user);
        System.out.println("update:"+userDao.selectOne(user).toString());
        String whereCondition = " id > '0'  ALLOW FILTERING ;";
        List<User> list =userDao.selectList(whereCondition);
        for (int i =0 ; i <list.size() ;i++ ) {
          System.out.println("selectList:"+list.get(i).toString());
        }
        boolean delete = userDao.delete(user.getId());
        System.out.println("delete result:"+delete);
    }

}
