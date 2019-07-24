package hello.java.designpattern.expression;

import java.util.HashMap;

public class Context {
    private HashMap map = new HashMap();

    public void assign(String key, String value) {
        //往环境类中设值
    }

    public String  get(String key) {
        //获取存储在环境类中的值
        return "";
    }
}
