package hello.java.designpattern.observer;

public class ObserverDemo {
    public static void main(String[] args) {
        Subject subject=new ConcreteSubject();
        Observer obs=new ConcreteObserver();
        subject.add(obs);
        subject.notifyObserver("data1");
    }
}
