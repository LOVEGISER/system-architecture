package hello.java.lock;


import java.util.concurrent.locks.ReentrantLock;
/**
 * Created by alex on 2019/3/2.
 */
public class InterruptiblyLock  {
    public   ReentrantLock lock1 = new ReentrantLock();
    public   ReentrantLock lock2 = new ReentrantLock();
    public Thread lock1 (){
        Thread t = new Thread(new Runnable(){
            public void run(){
                try {
                        lock1.lockInterruptibly(); // 如果当前线程未被 中断，则获取锁。
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        lock2.lockInterruptibly();
                        System.out.println(Thread.currentThread().getName()+"，执行完毕！");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 查询当前线程是否保持此锁。
                    if (lock1.isHeldByCurrentThread()) {
                        lock1.unlock();
                    }
                    if (lock2.isHeldByCurrentThread()) {
                        lock2.unlock();
                    }
                    System.out.println(Thread.currentThread().getName() + "，退出。");
                }
            }
        });
        t.start();
        return t;
    }

    public Thread lock2 (){
        Thread t = new Thread(new Runnable(){
            public void run(){
                try {
                        lock2.lockInterruptibly();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        lock1.lockInterruptibly();
                        System.out.println(Thread.currentThread().getName()+"，执行完毕！");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 查询当前线程是否保持此锁。
                    if (lock1.isHeldByCurrentThread()) {
                        lock1.unlock();
                    }
                    if (lock2.isHeldByCurrentThread()) {
                        lock2.unlock();
                    }
                    System.out.println(Thread.currentThread().getName() + "，退出。");
                }
            }
        });
        t.start();
        return t;
    }
    public static void main(String[] args) throws InterruptedException {
        long time = System.currentTimeMillis();
        InterruptiblyLock interruptiblyLock = new InterruptiblyLock();
        Thread thread1  = interruptiblyLock.lock1();
        Thread thread2  =  interruptiblyLock.lock2();
        //自旋一段时间，如果等待时间过长，可能发生了死锁等问题，主动中断，释放锁
        while (true){
            if(System.currentTimeMillis() - time >=3000){
                thread2.interrupt(); // 中断线程1
            }
        }
    }
}