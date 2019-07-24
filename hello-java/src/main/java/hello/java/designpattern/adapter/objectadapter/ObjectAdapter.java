package hello.java.designpattern.adapter.objectadapter;

import hello.java.designpattern.adapter.classadapter.Source;
import hello.java.designpattern.adapter.classadapter.Targetable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectAdapter  implements Targetable {
    private final static Log logger = LogFactory.getLog(ObjectAdapter.class);
    private Source source;

    public ObjectAdapter(Source source){
        super();
        this.source = source;
    }
    @Override
    public void editTextFile() {
         this.source.editTextFile();
    }

    @Override
    public void editWordFile() {
        logger.info("a word file editing");
    }
}
