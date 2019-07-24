package com.alex.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZookeeperApplicationTests {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired ZkUtils zkUtils;

    @Test
    public void TestZookeeper() {

       //zkUtils.createNode(CreateMode.PERSISTENT,"/test2","testData");
        byte[] data = zkUtils.getNodeData("/test2");
        zkUtils.watchPath("/test2", new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                logger.info(treeCacheEvent.getType()+";"+treeCacheEvent.getData());
            }
        });
        logger.info(new String(data));
        zkUtils.setNodeData("/test2","newtestData".getBytes());
        zkUtils.deleteNode("/test2");
    }

}
