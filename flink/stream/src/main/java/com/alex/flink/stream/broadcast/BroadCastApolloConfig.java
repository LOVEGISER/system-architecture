/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alex.flink.stream.broadcast;

import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@SuppressWarnings("serial")
public class BroadCastApolloConfig {

    final static Logger logger = LoggerFactory.getLogger(BroadCastApolloConfig.class);
    public static void main(String[] args) throws Exception {
        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> text = env.addSource(new DataSource());
        //广播配置
        MapStateDescriptor<String, String> apolloConfigDescriptor = new MapStateDescriptor<>("apolloConfigDescriptor",
                        BasicTypeInfo.STRING_TYPE_INFO,
                        BasicTypeInfo.STRING_TYPE_INFO);
        BroadcastStream<Tuple2<String, String>> configBroadcastStream = env.addSource(new ApolloSource("click")).broadcast(apolloConfigDescriptor);
        BroadcastConnectedStream<String, Tuple2<String, String>> connect = text.connect(configBroadcastStream);
        SingleOutputStreamOperator<Tuple2<String, String>> outStream = connect.process(new ConfigBroadcast(apolloConfigDescriptor));
        outStream.addSink(new SinkLog());
        env.execute("BroadCastApolloConfig");
    }
}
