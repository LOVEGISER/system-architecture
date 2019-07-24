package hello.java.lock;

import java.util.concurrent.locks.ReentrantLock;

public class CanRenterLock {
    public static  ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) {
            try {
                lock.lock();
                lock.lock();
            }finally {
                lock.unlock();
//                lock.unlock();
            }

    }
}
