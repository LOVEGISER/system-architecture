package hello.java.designpattern.mediator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//房客类
public class ColleagueTenant extends Colleague {
    private final static Log logger = LogFactory.getLog(ColleagueTenant.class);
    @Override
    public boolean operation(String message) {
        logger.info("tenant receive a message form mediator:"+message);
        return true;
    }
}
