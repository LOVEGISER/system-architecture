package hello.java.general;

import java.util.Date;

public class GeneralMethod {
    public static void main(String[] args) {
        GeneralClass<Object> gen =new GeneralClass<Object>();

        //generalMethod("1",2,new Wroker());
    }
    // 泛型方法 printArray
    public static < T > void generalMethod( T ... inputArray )
    {
        for ( T element : inputArray ){

            if (element instanceof Integer) {
                System.out.println("处理Integer类型数据中...");
            } else if (element instanceof String) {
                System.out.println("处理String类型数据中...");
            } else if (element instanceof Double) {
                System.out.println("处理Double类型数据中...");
            } else if (element instanceof Float) {
                System.out.println("处理Float类型数据中...");
            } else if (element instanceof Long) {
                System.out.println("处理Long类型数据中...");
            } else if (element instanceof Boolean) {
                System.out.println("处理Boolean类型数据中...");
            } else if (element instanceof Date) {
                System.out.println("处理Date类型数据中...");
            }
            else if (element instanceof Wroker) {
                System.out.println("处理Wroker类型数据中...");
            }

        }
    }

}
