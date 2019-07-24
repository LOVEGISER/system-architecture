package hello.java.designpattern.facade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Starter {
    private final static Log logger = LogFactory.getLog(Starter.class);
    private Dashboard dashboard;
    private Engine engine;
    private SelfCheck selfCheck;
    public Starter(){
        this.dashboard = new Dashboard();
        this.engine = new Engine();
        this.selfCheck = new SelfCheck();
    }
    public void startup(){
        logger.info("car begine startup");
        engine.startup();
        dashboard.startup();
        selfCheck.startupCheck();
        logger.info("car startup finished");
    }
    public void shutdown(){
        logger.info("car begine shutdown");
        selfCheck.shutdowncheck();
        engine.shutdown();
        dashboard.shutdown();
        logger.info("car shutdown finished");
    }
}
