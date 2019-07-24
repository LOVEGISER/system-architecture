package hello.java.designpattern.template;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SaveMoney extends AbstractTemplate {
    private final static Log logger = LogFactory.getLog(AbstractTemplate.class);

    @Override
    public void handleBusiness() {
        logger.info("save money form bank.");
    }
}
