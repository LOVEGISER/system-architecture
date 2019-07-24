package hello.java.designpattern.facade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SelfCheck {
    private final static Log logger = LogFactory.getLog(SelfCheck.class);
    public void startupCheck(){
        logger.info(" startup check finished.");
    }

    public void shutdowncheck(){
        logger.info("shutdown check finished.");
    }
}
