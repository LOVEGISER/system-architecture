package hello.java.innerclass;

import java.util.HashMap;
import java.util.Map;

public class OuterClass {
    private static String className ="staticInnerClass";
    private   String numberlassName ="numberlass";


    public static void main(String[] args) {
        //调用静态内部类
//        OuterClass.StaticInnerClass staticInnerClass = new OuterClass.StaticInnerClass();
//        staticInnerClass.getClassName();
        //调用成员内部类
//        OuterClass.MemberInnerClass mberInnerClass = new OuterClass.MemberInnerClass();
//        mberInnerClass.getClassName();

    }
    //定义一个成员内部类
    public  class MemberInnerClass {
        public void getClassName() {
            System.out.println("className:"+className );
        }
    }

    //定义一个静态内部类
    public static class StaticInnerClass {
        public void getClassName() {
            System.out.println("className:"+className );
        }
    }
}




