package hello.java.lock;

import hello.java.designpattern.builder.BuilderDemo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.UUID;


/**
 * Redis distributed lock implementation.
 *
 * @author alex
 */
public class RedisLock {
    private final static Log logger = LogFactory.getLog(BuilderDemo.class);

    private Jedis jedis;

    public RedisLock(Jedis jedis) {
        this.jedis = jedis;

    }


    /**
     *获取锁
     */
    public synchronized boolean lock(String lockId){
        //设置锁
        Long status = jedis.setnx(lockId,System.currentTimeMillis()+"") ;
        if (0 == status){//有别人正在使用该锁，获取锁失败
            return false;
        }else{
            return true;//创建/获取锁成功，锁id=lockId
        }
    }

    /**
     *释放锁
     */
    public synchronized boolean unlock(String lockId) {
        String lockValue = jedis.get(lockId);
        if (lockValue != null) {//释放锁成功
            jedis.del(lockId);
            return true;
        }else {
            return false;//释放锁失败
        }
    }

    public static void main(String[] args) {
        JedisPoolConfig jcon = new JedisPoolConfig();
        JedisPool jp = new JedisPool(jcon,"127.0.0.1",6379);
        Jedis jedis = jp.getResource();
        RedisLock lock = new RedisLock(jedis);
        String lockId = "123";
        long expireTime  = 100;//该超时时间要大于锁代码执行时间，
        try {

            if (lock.lock(lockId)) {
                //需要加锁的代码
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock(lockId);

        }
        logger.info("ok");
    }

}
