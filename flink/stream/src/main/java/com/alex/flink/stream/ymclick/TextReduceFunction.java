package com.alex.flink.stream.ymclick;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: TextReduceFunction
 * @Package: com.yeahmobi.window
 * @Description: TODO
 * @author: Mo.Lee
 * @date: 2019/6/20
 * @since: 1.0
 */
public class TextReduceFunction implements ReduceFunction<Tuple3<String, Integer, Long>>, CheckpointedFunction {
    private static final Logger logger = LoggerFactory.getLogger(TextReduceFunction.class);

    //托管状态
    private transient ListState<Tuple3<String, Integer, Long>> listState;

    private List<Tuple3<String, Integer, Long>> listBufferElements = new ArrayList<>();
    @Override
    public Tuple3<String, Integer, Long> reduce(
            Tuple3<String, Integer, Long> value1,
            Tuple3<String, Integer, Long> value2) throws Exception {
        logger.info("reduce data:"+listBufferElements.size());
        Tuple3<String, Integer, Long> tmp = Tuple3.of("", 0, -1L);
        if (listBufferElements.size() > 0) {
            for(int i = 0 ; i < listBufferElements.size(); i ++) {
                listBufferElements.get(i);
                tmp = Tuple3.of(listBufferElements.get(i).f0, tmp.f1 + listBufferElements.get(i).f1, -1L);
            }
            listBufferElements.clear();
        }
        Tuple3<String, Integer, Long> tmp1 = Tuple3.of(value1.f0, value1.f1 + value2.f1, -1L);
        //return Tuple5.of("-1", value1.f1 + value2.f1, -1L, value2.f3, value2.f4);
        return Tuple3.of(tmp1.f0, tmp1.f1 + tmp.f1, -1L);
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        listState.clear();
        for(Tuple3<String, Integer, Long> e: listBufferElements){
            listState.add(e);
        }
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<Tuple3<String, Integer, Long>> listStateDescriptor =
                new ListStateDescriptor<Tuple3<String, Integer, Long>>(
                        "listStateDescriptor",
                        TypeInformation.of(new TypeHint<Tuple3<String, Integer, Long>>(){}));

        listState = context.getOperatorStateStore().getListState(listStateDescriptor);
        if (context.isRestored()) {
            listState.get().forEach(e -> listBufferElements.add(e));
        }
    }

    /*private static class SumFun implements ReduceFunction<Tuple5<String, Long, Long, String, Long>> {
        private static final long serialVersionUID = 1L;

        @Override
        public Tuple5<String, Long, Long, String, Long> reduce(Tuple5<String, Long, Long, String, Long> value1, Tuple5<String, Long, Long, String, Long> value2) throws Exception {
            return Tuple5.of("-1", value1.f1 + value2.f1, -1L, value2.f3, value2.f4);
        }
    }*/

}
