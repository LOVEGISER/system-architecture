package hello.java.designpattern.command;

public class CommandTest {
    public static void main(String[] args) {
        //定义命令接受者/执行者
        Receiver receiver = new Receiver();
        //定义命令实现类
        Command cmd = new ConcreteCommand(receiver);
        //定义命令调用者
        Invoker invoker = new Invoker(cmd);
        //命令调用
        invoker.action("command1");
    }
}
