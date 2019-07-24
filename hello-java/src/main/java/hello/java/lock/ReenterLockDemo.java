package hello.java.lock;


import java.util.concurrent.locks.ReentrantLock;
/**
 * Created by alex on 2019/3/2.
 */
public class ReenterLockDemo implements Runnable{
    public static ReentrantLock lock = new ReentrantLock();
    public static int i = 0;
    public void run() {
        for (int j = 0;j<100000;j++) {
            lock.lock();
//            lock.lock();
            try {
                i++;
            }finally {
                lock.unlock();
//                lock.unlock();
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ReenterLockDemo reenterLock = new ReenterLockDemo();
        Thread t1 = new Thread(reenterLock);
        t1.start();
        t1.join();
        System.out.println(i);
    }
}
