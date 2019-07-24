package hello.java.designpattern.template;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TakeMoney extends AbstractTemplate {
    private final static Log logger = LogFactory.getLog(AbstractTemplate.class);

    @Override
    public void handleBusiness() {
        logger.info("take money form bank.");
    }
}
