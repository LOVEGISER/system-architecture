package hello.java.designpattern.iterator;

public interface Collection {

    public Iterator iterator();

    /*取得集合元素*/
    public Object get(int i);
    /*向集合添加元素*/
    public boolean add(Object object);
    /*取得集合大小*/
    public int size();
}