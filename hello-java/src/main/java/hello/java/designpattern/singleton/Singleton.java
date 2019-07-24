package hello.java.designpattern.singleton;

public class Singleton {
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }
    private Singleton() {
    }
    public static final Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}