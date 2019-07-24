package hello.java.designpattern.abstractfactory;

public class PhoneFactory extends AbstractFactory{
    @Override
    public Phone createPhone(String brand) {
        if ("HuaWei".equals(brand)){
            return new PhoneHwaiWei();
        }else if("Apple".equals(brand)){
            return new PhoneApple();
        }else{
            return  null;
        }

    }

    @Override
    public Computer createComputer(String brand){
            return  null;
    }
}
