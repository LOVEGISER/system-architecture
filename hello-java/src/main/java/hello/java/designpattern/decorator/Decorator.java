package hello.java.designpattern.decorator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Decorator implements Sourceable{

    private Sourceable source;
    private final static Log logger = LogFactory.getLog(Decorator.class);
    public Decorator(Sourceable source){
        super();
        this.source = source;
    }
    @Override
    public void createComputer() {
        source.createComputer();
        //创建完电脑后给电脑装上系统
        logger.info("make system.");
    }
}
