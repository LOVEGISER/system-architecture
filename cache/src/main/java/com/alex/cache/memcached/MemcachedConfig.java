package com.alex.cache.memcached;


import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemcachedConfig {

    @Autowired
    private MemcachedProperties memcachedProperties;

    // 构建builder
    @Bean
    public MemcachedClientBuilder getXMBuilder(){
        MemcachedClientBuilder memcachedClientBuilder = null;
        try{
            String servers = memcachedProperties.getServers();
            System.out.println("servers="+servers);
            memcachedClientBuilder = new XMemcachedClientBuilder(servers);
            // 开启/关闭failure模式
//            memcachedClientBuilder.setFailureMode(false);
//            memcachedClientBuilder.setSanitizeKeys(memcachedProperties.getSantizeKeys());
//            memcachedClientBuilder.setConnectionPoolSize(memcachedProperties.getPoolSize());
            memcachedClientBuilder.setCommandFactory(new BinaryCommandFactory());
            memcachedClientBuilder.setOpTimeout(3000);
            memcachedClientBuilder.setSessionLocator(new KetamaMemcachedSessionLocator());

            // 诸多XMemcached配置
            return memcachedClientBuilder;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // client
    @Bean
    public MemcachedClient getXMClient(MemcachedClientBuilder memcachedClientBuilder){
        MemcachedClient memcachedClient = null;
        try{
            memcachedClient = memcachedClientBuilder.build();
            return memcachedClient;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
