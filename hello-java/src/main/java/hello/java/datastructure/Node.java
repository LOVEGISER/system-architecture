package hello.java.datastructure;



public class Node {

    private int value;
    private Node left;
    private Node right;

    public Node(){
    }
    public Node(Node left, Node right, int value){
        this.left = left;
        this.right = right;
        this.value = value;
    }
    public Node(int value){
        this(null, null, value);
    }

    public Node getLeft(){
        return this.left;
    }
    public void setLeft(Node left){
        this.left = left;
    }
    public Node getRight(){
        return this.right;
    }
    public void setRight(Node right){
        this.right = right;
    }
    public int getValue(){
        return this.value;
    }
    public void setValue(int value){
        this.value = value;
    }
}

