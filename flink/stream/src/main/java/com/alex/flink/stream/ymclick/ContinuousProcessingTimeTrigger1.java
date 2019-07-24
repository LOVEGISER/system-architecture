package com.alex.flink.stream.ymclick;

import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ContinuousProcessingTimeTrigger;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;


import java.util.Date;

/**
 * @Title: ContinuousProcessingTimeTrigger1
 * @Package: com.yeahmobi.window
 * @Description: TODO
 * @author: Mo.Lee
 * @date: 2019/6/17
 * @since: 1.0
 */
public class ContinuousProcessingTimeTrigger1 <W extends TimeWindow> extends Trigger<Object, W> {
    private static final long serialVersionUID = 1L;

    private final long interval;



    /** When merging we take the lowest of all fire timestamps as the new fire timestamp. */
    private final ReducingStateDescriptor<Long> stateDesc =
            new ReducingStateDescriptor<>("fire-time", new Min(), LongSerializer.INSTANCE);

    private ContinuousProcessingTimeTrigger1(long interval) {
        this.interval = interval;
    }

    @Override
    public TriggerResult onElement(Object x, long timestamp, W window, TriggerContext ctx) throws Exception {
        ReducingState<Long> fireTimestamp = ctx.getPartitionedState(stateDesc);

        timestamp = ctx.getCurrentProcessingTime();
        //System.out.println("1111================="+ x +"---------"+fireTimestamp.get());
        if (fireTimestamp.get() == null) {
            long start = timestamp - (timestamp % interval);
            long nextFireTimestamp = start + interval;

            ctx.registerProcessingTimeTimer(nextFireTimestamp);

            fireTimestamp.add(nextFireTimestamp);

            return TriggerResult.CONTINUE;
        }

        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long time, W window, TriggerContext ctx) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long time, W window, TriggerContext ctx) throws Exception {
        ReducingState<Long> fireTimestamp = ctx.getPartitionedState(stateDesc);
        Date date = new Date(time);
        if (fireTimestamp.get().equals(time)) {
            fireTimestamp.clear();
            fireTimestamp.add(time + interval);
            ctx.registerProcessingTimeTimer(time + interval);

            if(window.getEnd() == time){
                //System.out.println("2222=================" + time + "----" + window.getStart() + "----" + window.getEnd());
                System.out.println("fire_and_purge window---" + window.getStart()+ "----" + window.getEnd() + "----" + time);
                return TriggerResult.FIRE_AND_PURGE;
            }else{
                System.out.println("fire window---" + window.getStart()+ "----" + window.getEnd() + "----" + time);
                return TriggerResult.FIRE;
            }
        }
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(W window, TriggerContext ctx) throws Exception {
        ReducingState<Long> fireTimestamp = ctx.getPartitionedState(stateDesc);
        long timestamp = fireTimestamp.get();
        ctx.deleteProcessingTimeTimer(timestamp);
        fireTimestamp.clear();
    }

    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    public void onMerge(W window,
                        OnMergeContext ctx) {
        ctx.mergePartitionedState(stateDesc);
    }

    @VisibleForTesting
    public long getInterval() {
        return interval;
    }

    @Override
    public String toString() {
        return "ContinuousProcessingTimeTrigger(" + interval + ")";
    }

    /**
     * Creates a trigger that continuously fires based on the given interval.
     *
     * @param interval The time interval at which to fire.
     * @param <W> The type of {@link Window Windows} on which this trigger can operate.
     */
    public static <W extends TimeWindow> ContinuousProcessingTimeTrigger1<W> of(Time interval) {
        return new ContinuousProcessingTimeTrigger1<>(interval.toMilliseconds());
    }

    private static class Min implements ReduceFunction<Long> {
        private static final long serialVersionUID = 1L;

        @Override
        public Long reduce(Long value1, Long value2) throws Exception {
            return Math.min(value1, value2);
        }
    }
}
