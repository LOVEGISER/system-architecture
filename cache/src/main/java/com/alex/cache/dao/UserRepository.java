package com.alex.cache.dao;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 继承JpaRepository来完成对数据库表的操作
 */
public interface UserRepository extends JpaRepository<User,String> {


}
