package hello.java.designpattern.adapter.classadapter;


import hello.java.designpattern.adapter.objectadapter.ObjectAdapter;

public class ClassAdapterDemo {
    public static void main(String[] args) {
//        Targetable target = new Adapter();
//        target.editTextFile();
//        target.editWordFile();

        Source source = new Source();
        Targetable target = new ObjectAdapter(source);
        target.editWordFile();
        target.editTextFile();
    }
}
