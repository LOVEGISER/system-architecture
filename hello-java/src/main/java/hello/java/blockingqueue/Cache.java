package hello.java.blockingqueue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

public class Cache {
    public ConcurrentHashMap map = new ConcurrentHashMap();

    private long expireTime = 1000;
    public DelayQueue queue = new DelayQueue();


}
