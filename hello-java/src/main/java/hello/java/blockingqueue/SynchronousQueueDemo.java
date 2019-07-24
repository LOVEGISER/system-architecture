package hello.java.blockingqueue;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;

public class SynchronousQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>();
        new Producter(queue).start();
        new Customer(queue).start();
    }
    static class Producter extends Thread{
        SynchronousQueue<Integer> queue;
        public Producter(SynchronousQueue<Integer> queue){
            this.queue = queue;
        }
        @Override
        public void run(){
            while(true){
                try {
                    int product = new Random().nextInt(1000);
                    //生产一个随机数作为数据放入队列，具体应用调整成业务数据即可。
                    queue.put(product);
                    System.out.println("product a data:"+product);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(queue.isEmpty());
            }
        }
    }
    static class Customer extends Thread{
        SynchronousQueue<Integer> queue;
        public Customer(SynchronousQueue<Integer> queue){
            this.queue = queue;
        }
        @Override
        public void run(){
            while(true){
                try {
                   int data = queue.take();
                    System.out.println("customer a data:"+data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
