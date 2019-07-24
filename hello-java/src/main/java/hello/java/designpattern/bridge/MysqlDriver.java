package hello.java.designpattern.bridge;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MysqlDriver implements Driver{
    private final static Log logger = LogFactory.getLog(MysqlDriver.class);
    @Override
    public void executeSQL() {
        logger.info( "execute sql by mysql driver");
    }
}
