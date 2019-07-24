package hello.java.designpattern.factory;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FactoryDemo {


    private final static Log logger = LogFactory.getLog(FactoryDemo.class);
    public static void main(String[] args) {
        Factory factory = new Factory();
        Phone huawei =  factory.createPhone("HuaWei");
        Phone iphone =  factory.createPhone("Apple");
        logger.info(huawei.brand());
        logger.info(iphone.brand());
    }
}
