package hello.java.designpattern.composite;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CompositeDemo {
    private final static Log logger = LogFactory.getLog(CompositeDemo.class);
    public static void main(String[] args) {
        TreeNode nodeA = new TreeNode("A");
        TreeNode nodeB = new TreeNode("B");
        nodeA.add(nodeB);
        logger.info(JSON.toJSONString(nodeA));
    }
}
