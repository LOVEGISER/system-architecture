package hello.java.designpattern.builder;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ComputerConcreteBuilder implements ComputerBuilder {
    Computer computer;
    private final static Log logger = LogFactory.getLog(ComputerConcreteBuilder.class);
    public ComputerConcreteBuilder() {
        computer =new  Computer();
    }

    @Override
    public void buildpu() {
        logger.info("buildpu......");
        computer.setCpu("8core");
    }

    @Override
    public void buildemory() {
        logger.info("buildemory......");
        computer.setMemory("16G");
    }

    @Override
    public void buildDisk() {
        logger.info("buildDisk......");
        computer.setDisk("1TG");
    }

    @Override
    public Computer buildComputer() {
        return computer;
    }
}
