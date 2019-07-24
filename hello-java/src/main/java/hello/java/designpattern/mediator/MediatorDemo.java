package hello.java.designpattern.mediator;

public class MediatorDemo {
    public static void main(String[] args) {
        //定义房客同事类
        Colleague colleagueTenant = new ColleagueTenant();
        //定义房东同事类
        Colleague colleagueLandlord = new ColleagueLandlord();
        //创建一个具体的中间者，这里可以理解为房屋中介
        ConcreteMediator concreteMediator = new ConcreteMediator(colleagueTenant, colleagueLandlord);
        boolean resoult = concreteMediator.notifyColleagueTenant("想租2室1厅的吗？");
       if(resoult){
           concreteMediator.notifyColleagueLandlord("租客对面积满意");
       }else{
           concreteMediator.notifyColleagueLandlord("租客对面积不满意");
       }
    }
}
