package hello.java.designpattern.abstractfactory;

public class ComputerFactory extends AbstractFactory{
    @Override
    public Phone createPhone(String brand) {
            return  null;
    }

    @Override
    public Computer createComputer(String brand){
        if ("HuaWei".equals(brand)){
            return new ComputerHwaiWei();
        }else if("Apple".equals(brand)){
            return new ComputerApple();
        }else{
            return  null;
        }
    }
}
