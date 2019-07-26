package hello.java.concurrent;

public class Person {
     volatile int age ;
     volatile String name ;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }


    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
