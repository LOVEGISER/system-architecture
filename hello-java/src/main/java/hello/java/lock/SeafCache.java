package hello.java.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SeafCache {
        private final Map<String, Object> cache = new HashMap<String, Object>();
        private final ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
        private final Lock readLock = rwlock.readLock();    //读锁
        private final Lock writeLock = rwlock.writeLock();    //写锁
       //读数据的时候加读锁
        public Object get(String key) {
            readLock.lock();
            try { return cache.get(key); }
            finally { readLock.unlock(); }
        }
       //写的时候加写锁
        public Object put(String key, Object value) {
            writeLock.lock();
            try { return cache.put(key, value); }
            finally { writeLock.unlock(); }
        }
}
