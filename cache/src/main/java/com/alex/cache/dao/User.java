package com.alex.cache.dao;

import javax.persistence.*;

/**
 * @author: alex
 * @create: 2019-09-06 16:21
 * @description:
 **/
/*
    使用JPA注解配置映射关系
    @Entity 告诉jpa这是一个实体类（和数据库表映射的类）
    @Table 指定和哪个数据表对应；如果省略name,默认表名是user
 */

@Entity
@Table(name = "user")
public class User {

    /**
     * 自增主键id
     */
    @Id
    private String id;
    //数据库列名称和长度
    @Column(name = "name", length = 50)
    private String name;

    /**
     * 省略默认列名就是属性名
     */
    @Column
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
