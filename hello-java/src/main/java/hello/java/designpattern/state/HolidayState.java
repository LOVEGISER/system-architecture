package hello.java.designpattern.state;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HolidayState extends AbstractState {
    private final static Log logger = LogFactory.getLog(HolidayState.class);
    public void action(Context context) {
        logger.info("state change to holiday state ");
        logger.info("holiday state actions is travel，shopping，watch television...");
    }
}
