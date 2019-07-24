package hello.java.designpattern.adapter.interfaceadapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SourceSub2 extends AbstractAdapter{
    private final static Log logger = LogFactory.getLog(SourceSub2.class);
    @Override
    public void editWordFile() {
        logger.info("a word file editing");
    }
}