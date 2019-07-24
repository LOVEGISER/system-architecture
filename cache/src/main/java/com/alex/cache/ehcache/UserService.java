package com.alex.cache.ehcache;

import com.alex.cache.dao.User;
import com.alex.cache.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserRepository userRepository;

    @CachePut(value = "user", key = "#user.id")
    public User save(User user) {
        User userAdd = userRepository.save(user);
        logger.info("user info add db and ehcache,key:" + userAdd.getId());
        return userAdd;
    }




    @Cacheable(value = "user", key = "#user.id")
    public User findOne(String id) {
        User userSearch = userRepository.getOne(id);
        return userSearch;
    }
}
