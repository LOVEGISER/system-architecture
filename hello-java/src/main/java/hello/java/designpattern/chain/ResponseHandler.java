package hello.java.designpattern.chain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseHandler extends AbstractHandler implements Handler {
    private final static Log logger = LogFactory.getLog(ResponseHandler.class);
    private String name;

    public ResponseHandler(String name) {
        this.name = name;
    }

    @Override
    public void operator() {
        logger.info("message response...");
        if(getHandler()!=null){//执行责任链下个流程
            getHandler().operator();
        }
    }
}
