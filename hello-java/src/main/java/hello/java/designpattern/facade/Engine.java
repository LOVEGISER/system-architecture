package hello.java.designpattern.facade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Engine {
    private final static Log logger = LogFactory.getLog(Engine.class);
    public void startup(){
        logger.info("engine startup......");
    }
    public void shutdown(){
        logger.info("engine shutdown......");
    }
}
