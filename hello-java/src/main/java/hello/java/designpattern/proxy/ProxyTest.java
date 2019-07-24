package hello.java.designpattern.proxy;

public class ProxyTest {
    public static void main(String[] args) {
        Company compay = new Proxy();
        compay.findWorker("Java");
    }
}
