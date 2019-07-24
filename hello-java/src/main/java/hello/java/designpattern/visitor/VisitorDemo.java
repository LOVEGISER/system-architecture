package hello.java.designpattern.visitor;

public class VisitorDemo {
    public static void main(String[] args) {
        Element element = new ProjectElement("mobike","share bicycle");
        element.accept(new CTOVisitor());
        element.accept(new CEOVisitor());
    }
}
