package hello.java.arithmetic.hash;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SeparateChainingHashTable<T> {
    private static final int DEFAULT_TABLE_SIZE = 10;
    private List<T>[] theLists;
    private int currentSize;
    //调用带参构造器
    public SeparateChainingHashTable() {
        this(DEFAULT_TABLE_SIZE);
    }
    //初始化一个数组,并把数组中每一个链表初始化
    public SeparateChainingHashTable(int size) {
        //初始化一个11位的数组
        theLists = new LinkedList[nextPrime(size)];
        for (int i = 0;i < theLists.length;i++) {
            theLists[i] = new LinkedList();
        }
    }
    private int myhash(T x) {
        //取得对象的hash值
        int hashVal = x.hashCode();
        //hash值对数组长度取模
        hashVal %= theLists.length;
        if (hashVal < 0) {
            hashVal += theLists.length;
        }
        return hashVal;
    }
    public void insert(T x) {
        //从链表数组中取得第该对象哈希值位的链表.
        List<T> whichList = theLists[myhash(x)];
        //如果该链表不包含该对象,则链表添加该对象
        if (!whichList.contains(x)) {
            whichList.add(x);
            //如果currentSize加1后大于数组的长度,扩容重新计算hash
            if (++currentSize > theLists.length)
                rehash();
        }
    }
    public void remove(T x) {
        List<T> whichList = theLists[myhash(x)];
        if (whichList.contains(x)) {
            whichList.remove(x);
            currentSize--;
        }
    }
    public boolean contains(T x) {
        List<T> whichList = theLists[myhash(x)];
        return whichList.contains(x);
    }
    public void makeEmpty() {
        for (int i = 0;i < theLists.length;i++) {
            theLists[i].clear();
        }
        currentSize = 0;
    }
    private void rehash() {
        List<T>[] oldLists = theLists;
        //进行一次扩容,扩容后长度为23,但是是一个新的数组
        theLists = new List[nextPrime(2 * theLists.length)];
        for(int j = 0;j < theLists.length;j++){
            //初始化新数组中的每一个链表
            theLists[j] = new LinkedList<T>();
        }
        //将新数组的currentSize归0
        currentSize = 0;
        //将原有的链表对象放入新数组中,并重新取模计算hash值
        for (int i = 0; i < oldLists.length; i++) {
            for (T item : oldLists[i]) {
                insert(item);
            }
        }
    }
    private static int nextPrime(int num) {
        if (num == 0 || num == 1 || num == 2) {
            return 2;
        }
        if (num % 2 == 0) {
            num++;
        }
        while (!isPrime(num)) {
            num += 2;
        }
        return num;
    }
    private static boolean isPrime(int num) {
        if (num == 2 || num == 3) {
            return true;
        }
        if (num == 1 || num % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= num; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
    public void printTable() {
        for(int i = 0;i < theLists.length;i++){
            System.out.println("-----");
            Iterator iterator = theLists[i].iterator();
            while(iterator.hasNext()){
                System.out.print(iterator.next() + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        Random random = new Random();
        SeparateChainingHashTable<Integer> hashTable = new SeparateChainingHashTable<Integer>();
        for (int i = 0; i < 30; i++) {
            Integer tmp = random.nextInt(30);
            hashTable.insert(tmp);
            System.out.printf(tmp + "\t");
        }
        hashTable.printTable();
    }
}
