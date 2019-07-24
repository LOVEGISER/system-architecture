package com.alex.cache.memcached;

import net.rubyeye.xmemcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowApi {

    @Autowired
    private MemcachedClient memcachedClient;

    /**
     * 新增
     * @param key
     * @param value
     */
    public void showAdd(String key, String value){
        try {
            memcachedClient.set(key, 0, value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String showQuery(String key){
        try {
            return memcachedClient.get(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}

