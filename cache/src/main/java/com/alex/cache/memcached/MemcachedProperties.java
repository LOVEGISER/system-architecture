package com.alex.cache.memcached;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "memcached")
public class MemcachedProperties {
    private String servers;
    private Integer poolSize;
    private Boolean  santizeKeys;

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public Boolean getSantizeKeys() {
        return santizeKeys;
    }

    public void setSantizeKeys(Boolean santizeKeys) {
        this.santizeKeys = santizeKeys;
    }
}
