package hello.java.designpattern.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Receiver {
    private final static Log logger = LogFactory.getLog(Receiver.class);
    public void action(String command){//接收并执行命令
        logger.info("command received, now execute command");
    }
}
