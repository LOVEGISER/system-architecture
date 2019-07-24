package hello.java.designpattern.chain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BusinessHandler extends AbstractHandler implements Handler {
    private final static Log logger = LogFactory.getLog(BusinessHandler.class);
    private String name;

    public BusinessHandler(String name) {
        this.name = name;
    }

    @Override
    public void operator() {
        logger.info("business info handler...");
        if(getHandler()!=null){//执行责任链下个流程
            getHandler().operator();
        }
    }
}
