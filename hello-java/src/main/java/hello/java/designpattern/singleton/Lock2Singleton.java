package hello.java.designpattern.singleton;

public class Lock2Singleton {

    private volatile static Lock2Singleton singleton;
    private Lock2Singleton (){}
    public static Lock2Singleton getSingleton() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Lock2Singleton();
                }
            }
        }
        return singleton;
    }

}
