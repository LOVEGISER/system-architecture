package hello.java.lock;


import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhengbinMac on 2017/3/2.
 */
public class IntLock implements Runnable{
    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();
    int lock;
    /**
     * 控制加锁顺序，产生死锁
     */
    public IntLock(int lock) {
        this.lock = lock;
    }
    public void run() {
        try {
            if (lock == 1) {
                lock1.lockInterruptibly(); // 如果当前线程未被 中断，则获取锁。
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock2.lockInterruptibly();
                System.out.println(Thread.currentThread().getName()+"，执行完毕！");
            } else {
                lock2.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock1.lockInterruptibly();
                System.out.println(Thread.currentThread().getName()+"，执行完毕！");
            }
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
    public static void main(String[] args) throws InterruptedException {
        IntLock intLock1 = new IntLock(1);
        IntLock intLock2 = new IntLock(2);
        Thread thread1 = new Thread(intLock1, "线程1");
        Thread thread2 = new Thread(intLock2, "线程2");
        thread1.start();
        thread2.start();
        Thread.sleep(1000);
        thread2.interrupt(); // 中断线程2
    }
}