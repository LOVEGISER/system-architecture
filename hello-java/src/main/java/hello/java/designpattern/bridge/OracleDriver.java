package hello.java.designpattern.bridge;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OracleDriver implements Driver{
    private final static Log logger = LogFactory.getLog(OracleDriver.class);
    @Override
    public void executeSQL() {
        logger.info( "execute sql by oracle driver");
    }
}
