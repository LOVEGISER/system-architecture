package hello.java.designpattern.decorator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Source implements Sourceable{
    private final static Log logger = LogFactory.getLog(Source.class);
    @Override
    public void createComputer() {
        logger.info("create computer by Source");
    }
}
