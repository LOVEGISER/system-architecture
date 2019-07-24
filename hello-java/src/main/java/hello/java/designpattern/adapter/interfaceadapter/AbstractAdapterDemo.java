package hello.java.designpattern.adapter.interfaceadapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AbstractAdapterDemo {
    private final static Log logger = LogFactory.getLog(AbstractAdapterDemo.class);
    public static void main(String[] args) {
        Sourceable source1 = new SourceSub1();
        Sourceable source2 = new SourceSub2();
        source1.editTextFile();
        source2.editWordFile();
    }
}
