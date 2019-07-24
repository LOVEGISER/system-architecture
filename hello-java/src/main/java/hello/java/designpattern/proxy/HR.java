package hello.java.designpattern.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HR implements Company {
    private final static Log logger = LogFactory.getLog(HR.class);
    @Override
    public void findWorker(String title) {
        logger.info("i need find a worker,title is："+title);
    }
}
