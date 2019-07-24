package hello.java.designpattern.adapter.interfaceadapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SourceSub1  extends AbstractAdapter{
    private final static Log logger = LogFactory.getLog(SourceSub1.class);
    @Override
    public void editTextFile() {
        logger.info("a text file editing");
    }
}
