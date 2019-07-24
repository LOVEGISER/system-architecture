package hello.java.designpattern.adapter.classadapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Adapter extends Source implements Targetable{
    private final static Log logger = LogFactory.getLog(Adapter.class);
    @Override
    public void editWordFile() {
        logger.info("a word file editing");
    }
}
