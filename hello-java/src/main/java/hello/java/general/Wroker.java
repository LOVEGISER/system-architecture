package hello.java.general;

import java.io.*;

public class Wroker implements Serializable {
    //序列化id
    private static final long serialVersionUID = 123456789L;
   //name属性将被序列化
    private String name;
    //transient修饰的变量不会被序列化
    private transient  int salary;
    //静态变量属于类信息，不属于对象的"状态"，因此不会被序列化
    static int age =100;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws Exception {
        //序列化数据到磁盘
        FileOutputStream fos = new FileOutputStream("worker.out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Wroker testObject = new Wroker();
        testObject.setName("alex");
        oos.writeObject(testObject);
        oos.flush();
        oos.close();
       //反序列化磁盘数据并解析状态
        FileInputStream fis = new FileInputStream("worker.out");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Wroker deTest = (Wroker) ois.readObject();
        System.out.println(deTest.getName());

    }


}


