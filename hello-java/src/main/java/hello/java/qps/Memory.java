package hello.java.qps;

import java.util.concurrent.ConcurrentHashMap;

public class Memory {
   static ConcurrentHashMap map = new ConcurrentHashMap();

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();
          for(int i = 0; i< 200 ;i++) {
              new Thread(new Runnable(){//相当于是实现runnable接口
                  @Override
                  public void run(){
                     for (int j = 0 ; j < 1000000;j++){
                         map.put(j,j);
                     }
                     System.out.println("mapsize:"+map.size()+";time"+(System.currentTimeMillis()-startTime));
                  }
              }).start();
          }
    }
}
