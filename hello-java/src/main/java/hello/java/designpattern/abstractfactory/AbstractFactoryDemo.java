package hello.java.designpattern.abstractfactory;

import hello.java.designpattern.factory.FactoryDemo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AbstractFactoryDemo {
    private final static Log logger = LogFactory.getLog(AbstractFactoryDemo.class);
    public static void main(String[] args) {

        AbstractFactory phoneFactory = new PhoneFactory();
        Phone phoneHuawei =   phoneFactory.createPhone("HuaWei");
        Phone phoneApple = phoneFactory.createPhone("Apple");
        logger.info(phoneHuawei.call());
        logger.info(phoneApple.call());


        AbstractFactory computerFactory = new ComputerFactory();
        Computer computerHuawei =   computerFactory.createComputer("HuaWei");
        Computer computerApple = computerFactory.createComputer("Apple");
        logger.info(computerApple.internet());
        logger.info(computerApple.internet());
    }
}
