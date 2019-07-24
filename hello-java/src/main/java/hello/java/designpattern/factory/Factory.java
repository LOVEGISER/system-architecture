package hello.java.designpattern.factory;

public class Factory {
   public Phone createPhone(String phoneName){
       if ("HuaWei".equals(phoneName)){
           return new HuaWei();
       }else if("Apple".equals(phoneName)){
           return new Iphone();
       }else{
           return  null;
       }
   };

}

