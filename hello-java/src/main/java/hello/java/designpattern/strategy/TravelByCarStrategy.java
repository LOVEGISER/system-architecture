package hello.java.designpattern.strategy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TravelByCarStrategy implements  TravelStrategy{
    private final static Log logger = LogFactory.getLog(TravelByCarStrategy.class);
    @Override
    public void travelMode() {
        logger.info("travel by car");
    }
}
