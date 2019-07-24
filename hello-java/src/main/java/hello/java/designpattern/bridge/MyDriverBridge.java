package hello.java.designpattern.bridge;

public    class MyDriverBridge extends DriverManagerBridge {
    public void execute() {
        getDriver().executeSQL();
    }
}
