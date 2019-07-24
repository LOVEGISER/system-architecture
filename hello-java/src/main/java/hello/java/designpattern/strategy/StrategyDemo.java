package hello.java.designpattern.strategy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StrategyDemo {
    private final static Log logger = LogFactory.getLog(StrategyDemo.class);
    public static void main(String[] args) {
        Context context = new Context();
        TravelStrategy travelByAirStrategy = new TravelByAirStrategy();
        //设置出行策略为飞机
        context.setTravelStrategy(travelByAirStrategy);
        context.travelMode();
        logger.info("change TravelStrategy to travelByCarStrategy......");
        //设置出行策略为开车自驾
        TravelStrategy travelByCarStrategy= new TravelByCarStrategy();
        context.setTravelStrategy(travelByCarStrategy);
        context.travelMode();
    }
}
