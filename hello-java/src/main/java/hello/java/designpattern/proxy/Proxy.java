package hello.java.designpattern.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public class Proxy implements Company {
    private final static Log logger = LogFactory.getLog(Proxy.class);
    private HR hr;
    public Proxy(){
        super();
        this.hr = new HR();
    }
    @Override
    public void findWorker(String title) {
        hr.findWorker(title);
        //通过中介找工人
        String worker = getWorker(title);
        logger.info("find a worker by proxy,worker name is :"+worker);
    }

    private String getWorker(String title) {
        Map<String, String> workerList = new HashMap<String, String>() {
            { put("Java", "张三");put("Python", "李四");put("Php", "王五"); }
        };
        return workerList.get(title);
    }
}