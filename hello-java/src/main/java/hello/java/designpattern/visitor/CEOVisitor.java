package hello.java.designpattern.visitor;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class CEOVisitor implements Visitor{
    private final static Log logger = LogFactory.getLog(CEOVisitor.class);
    @Override
    public void visit(ProjectElement element) {
        logger.info("CEO Visitor Element");
        element.signature("CEO",new Date());
        logger.info(JSON.toJSON(element));
    }
}
