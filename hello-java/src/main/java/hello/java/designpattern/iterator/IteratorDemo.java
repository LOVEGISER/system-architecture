package hello.java.designpattern.iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IteratorDemo {
    private final static Log logger = LogFactory.getLog(IteratorDemo.class);
    public static void main(String[] args) {
        //定义集合
        Collection collection = new ListCollection();
        //向集合中添加数据
        collection.add("object1");
        //使用迭代器遍历集合
        Iterator it = collection.iterator();
        while(it.hasNext()){
            logger.info(it.next());
        }
    }
}

