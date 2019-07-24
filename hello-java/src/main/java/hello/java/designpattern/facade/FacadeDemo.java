package hello.java.designpattern.facade;

public class FacadeDemo {
    public static void main(String[] args) {
        Starter starter = new Starter();
        starter.startup();
        System.out.println("*******************");
        starter.shutdown();
    }
}
