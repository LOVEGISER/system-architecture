package com.alex.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.nio.file.attribute.AclEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Component
public class ZkUtils {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private CuratorFramework client = null;


    @Value("${zookeeper.server}")
    private String server;
    @Value("${zookeeper.namespace}")
    private String namespace;
    @Value("${zookeeper.server}")
    private String digest;
    @Value("${zookeeper.sessionTimeoutMs}")
    private Integer sessionTimeoutMs = 60000;
    @Value("${zookeeper.connectionTimeoutMs}")
    private Integer connectionTimeoutMs = 6000;
    @Value("${zookeeper.maxRetries}")
    private Integer maxRetries = 3;
    @Value("${zookeeper.baseSleepTimeMs}")
    private Integer baseSleepTimeMs = 1000;


    @PostConstruct
    public void init() {
        if (client != null) {
            return;
        }
        //创建重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        //创建zookeeper客户端
        client = CuratorFrameworkFactory.builder().connectString(server)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(retryPolicy)
                .namespace(namespace)
                .build();
        client.start();
    }

    /**
     * 创建节点
     *
     * @param mode     节点类型
     *                 1、PERSISTENT 持久化目录节点，存储的数据不会丢失。
     *                 2、PERSISTENT_SEQUENTIAL顺序自动编号的持久化目录节点，存储的数据不会丢失
     *                 3、EPHEMERAL临时目录节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除
     *                 4、EPHEMERAL_SEQUENTIAL临时自动编号节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除，并且根据当前已近存在的节点数自动加 1，然后返回给客户端已经成功创建的目录节点名。
     * @param path     节点名称
     * @param nodeData 节点数据
     */
    public void createNode(CreateMode mode, String path, String nodeData) {
        try {
            //使用creatingParentContainersIfNeeded()之后Curator能够自动递归创建所有所需的父节点
            client.create().creatingParentsIfNeeded().withMode(mode).forPath(path, nodeData.getBytes("UTF-8"));
        } catch (Exception e) {
            logger.error("注册出错", e);
        }
    }

    /**
     * 创建节点
     *
     * @param mode 节点类型
     *             1、PERSISTENT 持久化目录节点，存储的数据不会丢失。
     *             2、PERSISTENT_SEQUENTIAL顺序自动编号的持久化目录节点，存储的数据不会丢失
     *             3、EPHEMERAL临时目录节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除
     *             4、EPHEMERAL_SEQUENTIAL临时自动编号节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除，并且根据当前已近存在的节点数自动加 1，然后返回给客户端已经成功创建的目录节点名。
     * @param path 节点名称
     */
    public void createNode(CreateMode mode, String path) {
        try {
            //使用creatingParentContainersIfNeeded()之后Curator能够自动递归创建所有所需的父节点
            client.create().creatingParentsIfNeeded().withMode(mode).forPath(path);
        } catch (Exception e) {
            logger.error("注册出错", e);
        }
    }

    /**
     * 删除节点数据
     *
     * @param path
     */
    public void deleteNode(final String path) {
        try {
            deleteNode(path, true);
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }


    /**
     * 删除节点数据
     *
     * @param path
     * @param deleteChildren 是否删除子节点
     */
    public void deleteNode(final String path, Boolean deleteChildren) {
        try {
            if (deleteChildren) {
                //guaranteed()删除一个节点，强制保证删除,
                // 只要客户端会话有效，那么Curator会在后台持续进行删除操作，直到删除节点成功
                client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
            } else {
                client.delete().guaranteed().forPath(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置指定节点的数据
     *
     * @param path
     * @param datas
     */
    public void setNodeData(String path, byte[] datas) {
        try {
            client.setData().forPath(path, datas);
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
    }

    /**
     * 获取指定节点的数据
     *
     * @param path
     * @return
     */
    public byte[] getNodeData(String path) {
        Byte[] bytes = null;
        try {
            client.getData().forPath(path);
            return client.getData().forPath(path);
        } catch (Exception ex) {
            logger.error("{}", ex);
        }
        return null;
    }

    /**
     * 获取数据时先同步
     *
     * @param path
     * @return
     */
    public byte[] synNodeData(String path) {
        client.sync();
        return getNodeData(path);
    }

    /**
     * 判断路径是否存在
     *
     * @param path
     * @return
     */
    public boolean isExistNode(final String path) {
        client.sync();
        try {
            return null != client.checkExists().forPath(path);
        } catch (Exception ex) {
            return false;
        }
    }


    /**
     * 获取节点的子节点
     *
     * @param path
     * @return
     */
    public List<String> getChildren(String path) {
        List<String> childrenList = new ArrayList<>();
        try {
            childrenList = client.getChildren().forPath(path);
        } catch (Exception e) {
            logger.error("获取子节点出错", e);
        }
        return childrenList;
    }


    /**
     * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
     */
    ExecutorService pool = Executors.newFixedThreadPool(2);

    /**
     * 监听数据节点的变化情况
     *
     * @param watchPath
     * @param listener
     */
    public void watchPath(String watchPath, TreeCacheListener listener) {
        //   NodeCache nodeCache = new NodeCache(client, watchPath, false);
        TreeCache cache = new TreeCache(client, watchPath);
        cache.getListenable().addListener(listener, pool);
        try {
            cache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
