package hello.java.designpattern.memento;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MementoDemo {
    private final static Log logger = LogFactory.getLog(MementoDemo.class);
    public static void main(String[] args) {
        // 创建原始类
        Original original = new Original("张三");
        // 创建备忘录
        Storage storage = new Storage(original.createMemento());
        // 修改原始类的状态
        logger.info("original value：" + original.getValue());
        original.setValue("李四");
        logger.info("update value：" + original.getValue());
        // 回复原始类的状态
        original.restoreMemento(storage.getMemento());
        logger.info("restore value：" + original.getValue());

    }
}
