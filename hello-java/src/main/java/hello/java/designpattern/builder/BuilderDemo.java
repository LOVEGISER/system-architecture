package hello.java.designpattern.builder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BuilderDemo {
    private final static Log logger = LogFactory.getLog(BuilderDemo.class);
    public static void main(String[] args) {
        ComputerDirector computerDirector = new ComputerDirector();
        ComputerBuilder computerConcreteBuilder = new ComputerConcreteBuilder();
        Computer computer = computerDirector.constructComputer(computerConcreteBuilder);
        logger.info(computer.getCpu());
        logger.info(computer.getDisk());
        logger.info(computer.getMemory());
    }
}
