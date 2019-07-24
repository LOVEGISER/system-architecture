package hello.java.designpattern.flyweight;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MemoryDemo {
    private final static Log logger = LogFactory.getLog(MemoryDemo.class);
    public static void main(String[] args) {
        //首次获取内存将重新创建
       Memory memory = MemoryFactory.getMemory(32);
       //使用完成后释放内存
       MemoryFactory.releaseMemory(memory.getId());
       //重新获取内存
       MemoryFactory.getMemory(32);
    }
}
