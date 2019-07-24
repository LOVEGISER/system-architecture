package hello.java.designpattern.clone;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CloneDemo {
    private final static Log logger = LogFactory.getLog(CloneDemo.class);
    public static void main(String[] args) {
        //浅复制
        Computer computer = new Computer("8core","16G","1TB");
        logger.info("before simple clone:"+computer.toString());
        Computer computerClone = (Computer)computer.clone();
        logger.info("after simple clone:"+computerClone.toString());
        //深复制
        Disk disk = new Disk("208G","2TB");
        ComputerDetail computerDetail = new ComputerDetail("12core","64G",disk);
        logger.info("before deep clone:"+computerDetail.toString());
        ComputerDetail computerDetailClone = (ComputerDetail)computerDetail.clone();
        logger.info("after deep clone:"+computerDetailClone.toString());
    }
}
