package hello.java.designpattern.template;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractTemplate {
    private final static Log logger = LogFactory.getLog(AbstractTemplate.class);

    public void templateMethod() //模板方法
    {
        checkNumber();
        queueUp();
        handleBusiness();
        serviceEvaluation();
    }

    public void checkNumber() //1：抽号
    {
        logger.info("checkNumber......");
    }

    public void queueUp() //2：排队
    {
        logger.info("queue up......");
    }

    public abstract void handleBusiness(); //3：业务办理

    public void serviceEvaluation() //4：服务评价
    {
        logger.info("business finished,servic evaluation......");
    }


}

