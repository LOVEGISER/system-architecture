package hello.java.blockingqueue;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class BlockingQueue {
    public static Queue queue = new ArrayBlockingQueue(10);
    final LinkedBlockingQueue linkueue = new LinkedBlockingQueue(10000);

    final PriorityBlockingQueue<Data> priorityQueue = new PriorityBlockingQueue<Data>();


    public static void main(String[] args) {
        Object o = new Object();
        //queue.put(o);
        queue.offer(o);
        queue.poll();


    }
}
