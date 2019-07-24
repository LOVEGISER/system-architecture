package hello.java.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class InvokeDemo {
    public static void main(String[] args) {
        try{
//            Class<?> clz = Class.forName("hello.java.reflect.Persion");
//            Object o = clz.newInstance();
//            Method m = clz.getMethod("setName", null);
//            m.invoke(o,"zs");


            Class clz = Class.forName("hello.java.reflect.Persion");
            Method method = clz.getMethod("setName",String.class);
            Constructor constructor = clz.getConstructor();
            Object object = constructor.newInstance();
            method.invoke(object, "alex");
            System.out.println(object.toString());
        }catch (Exception e){
               e.printStackTrace();
        }

    }

}

