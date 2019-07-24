package hello.java.blockingqueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayData implements Delayed {
    private Integer number;//数据排序规则
    private long delayTime = 50000;//5s延迟
    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    @Override
    public long getDelay(TimeUnit unit) {
        return this.delayTime;
    }
    @Override
    public int compareTo(Delayed o) {
        DelayData compare = (DelayData) o;
        return this.number.compareTo(compare.getNumber());
    }

    public static void main(String[] args) {
        // 创建延时队列
        DelayQueue<DelayData> queue = new DelayQueue<DelayData>();
        //实时添加数据
        queue.add(new DelayData());
        while (true){
            try {
                //5s后延迟获取数据
                DelayData data =  queue.take();
            }catch (Exception e){

            }
        }
    }
}
