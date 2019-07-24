package hello.java.designpattern.adapter.classadapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Source {
    private final static Log logger = LogFactory.getLog(Source.class);
    public void editTextFile() {//text文件编辑
        logger.info("a text file editing");
    }
}
