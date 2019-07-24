package hello.java.designpattern.chain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuthHandler extends AbstractHandler implements Handler {
    private final static Log logger = LogFactory.getLog(AuthHandler.class);
    private String name;
    public AuthHandler(String name) {
        this.name = name;
    }
    @Override
    public void operator() {
        logger.info("user auth...");
        if(getHandler()!=null){//执行责任链下一个流程
            getHandler().operator();
        }
    }
}
