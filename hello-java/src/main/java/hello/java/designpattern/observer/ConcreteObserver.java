package hello.java.designpattern.observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConcreteObserver implements Observer {
    private final static Log logger = LogFactory.getLog(ConcreteObserver.class);
    public void dataChange(String message) {
        logger.info("recive message:"+message);
    }
}