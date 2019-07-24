package hello.java.designpattern.state;

public class StateDemo {
    public static void main(String[] args) {
        //定义当前状态为工作状态
        Context context = new Context(new WorkState());
        context.action();
        //切换当前状态为修改状态
        context.setState(new HolidayState());
        context.action();
    }
}
