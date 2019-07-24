package hello.java.exception;

public class ExceptionDemo {
    public static void main(String[] args) {
        String str = "hello offer";
        int index = 10;
        if (index >= str.length())
        {
            //使用throw关键之在方法内抛出异常
            throw new StringIndexOutOfBoundsException();
        }else {
            str.substring(0,index);
        }


    }
}
