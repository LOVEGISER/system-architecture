package com.alex.cassandra.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;


/**
 * 1:create key space : CREATE KEYSPACE user_space WITH REPLICATION = {'class' : 'SimpleStrategy','replication_factor' : 1 };
 * 2:use key space :use user_space;
 * 3:create table:CREATE TABLE user ( id VARCHAR, username VARCHAR, PRIMARY KEY (id,username)) WITH CLUSTERING ORDER BY (username DESC)  AND default_time_to_live = 8640000;
 * 4:insert a data : INSERT INTO user (id, username) VALUES('1','zhangsan');
 * 5:select * from user where id > 1  ALLOW FILTERING
 * 6:select * from user where id = 1 order by username;
 */
@Repository
public class UserDao {
    @Autowired
    private CassandraTemplate cassandraTemplate;
    //插入一条数据，id对于数据库rowkey
    public  boolean inert(User user){
        try {
            cassandraTemplate.insert(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //根据id更新数据
    public  boolean update(User user){
        try {
            cassandraTemplate.update(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    //根据whereCondition查询数据列表
    public  List<User> selectList(String whereCondition){
        try {
           return cassandraTemplate.select("SELECT id, username FROM user where "+whereCondition, User.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //根据id（rowkey）查询一条数据
    public  User selectOne(User user){
        try {
            String cql = "SELECT id, username FROM user where id='"+user.getId()+"'";
            return cassandraTemplate.selectOne(cql, User.class);
        }catch (Exception e){
            return null;
        }
    }
   //根据id（rowkey）删除一条数据
    public  boolean delete(String id){
        try {
            cassandraTemplate.deleteById(id,User.class);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
