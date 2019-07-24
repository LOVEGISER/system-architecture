package hello.java.designpattern.facade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Dashboard {
    private final static Log logger = LogFactory.getLog(Dashboard.class);
    public void startup(){
        logger.info("dashboard startup......");
    }

    public void shutdown(){
        logger.info("dashboard shutdown......");
    }
}
