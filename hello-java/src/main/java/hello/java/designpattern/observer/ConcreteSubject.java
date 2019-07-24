package hello.java.designpattern.observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//具体目标
public class ConcreteSubject extends Subject {
    private final static Log logger = LogFactory.getLog(ConcreteSubject.class);
    public void notifyObserver(String message) {

        for(Object obs:observers) {
            logger.info("notify observer "+message+" change...");
            ((Observer)obs).dataChange(message);
        }
    }
}