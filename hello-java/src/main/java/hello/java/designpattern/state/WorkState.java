package hello.java.designpattern.state;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorkState extends AbstractState {
    private final static Log logger = LogFactory.getLog(WorkState.class);
    public void action(Context context) {
        logger.info("state change to work state ");
        logger.info("work state actions is meeting，design，coding...");
    }
}
