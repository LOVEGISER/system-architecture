package hello.java.designpattern.mediator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ColleagueLandlord extends Colleague {

    private final static Log logger = LogFactory.getLog(ColleagueLandlord.class);
    @Override
    public boolean operation(String message) {
        logger.info("landlord receive a message form mediator:"+message);
        return true;
    }
}
