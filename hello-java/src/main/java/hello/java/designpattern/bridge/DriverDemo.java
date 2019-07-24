package hello.java.designpattern.bridge;

public class DriverDemo {
    public static void main(String[] args) {
        DriverManagerBridge driverManagerBridge = new MyDriverBridge() ;
        //设置mysql驱动
        driverManagerBridge.setDriver(new MysqlDriver());
        driverManagerBridge.execute();
        //切换驱动到Oracle驱动
        driverManagerBridge.setDriver(new OracleDriver());
        driverManagerBridge.execute();
    }
}
