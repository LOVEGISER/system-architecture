package hello.java.lock;


import java.util.concurrent.atomic.AtomicInteger;

class AtomicIntegerDemo implements Runnable {
    //定义一个原子操作
    static AtomicInteger safeCounter = new AtomicInteger(0);
    public void run() {
        for (int m = 0; m < 1000000; m++) {
            safeCounter.getAndIncrement();
        }
    }
};

public class AtomicIntegerDemoTest {
    public static void main(String[] args) throws InterruptedException {
        AtomicIntegerDemo mt = new AtomicIntegerDemo();
        Thread t1 = new Thread(mt);
        Thread t2 = new Thread(mt);
        t1.start();
        t2.start();
        Thread.sleep(500);
        System.out.println(mt.safeCounter.get());
    }
}