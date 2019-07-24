package hello.java.designpattern.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Invoker {
    private final static Log logger = LogFactory.getLog(Invoker.class);
    private Command command;

    public Invoker(Command command) {
        this.command = command;
    }

    public void action(String commandMessage){
        logger.info("command sending...");
        command.exe(commandMessage);
    }
}
