package hello.java.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SynchronizedDemo {


    static ExecutorService executorService = Executors.newFixedThreadPool(4);

    String lockA = "lockA";
    String lockB = "lockB";
    public static void main(String[] args) {
       final SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
       //final SynchronizedDemo synchronizedDemo2 = new SynchronizedDemo();
        new Thread(new Runnable() {
             @Override
            public void run() {
                 synchronizedDemo.blockMethod1();
             }
         }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronizedDemo.blockMethod2();
            }
        }).start();

    }

   //synchronized修饰普通同步方法，锁是当前实例对象
    public static synchronized void generalMethod1() {
        try {
            for(int i = 1 ; i<3;i++) {
                System.out.println("generalMethod1 execute " +i+" time");
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //synchronized修饰普通同步方法，锁是当前实例对象
    public static synchronized void generalMethod2() {
        try {
            for(int i = 1 ; i<3;i++) {
                System.out.println("generalMethod2 execute "+i+" time");
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //静态同步方法，锁是当前类的class对象
    public static synchronized void staticMethod() {

    }

    //步方法块，锁是括号里面的对象
    public   void blockMethod1() {
        try {
            synchronized (lockA) {
                for(int i = 1 ; i<3;i++) {
                    System.out.println("Method 1 execute");
                    Thread.sleep(3000);
                    synchronized (lockB){}
                }
            }
            } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //步方法块，锁是括号里面的对象
    public   void blockMethod2() {
        try {
            synchronized (lockB) {
                for(int i = 1 ; i<3;i++) {
                    System.out.println("Method 2 execute");
                    Thread.sleep(3000);
                    synchronized (lockA){}
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
















