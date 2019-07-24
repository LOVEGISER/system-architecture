package com.alex.flink.stream.broadcast;


import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.apache.directory.api.util.Strings;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ApolloSource extends RichSourceFunction<Tuple2<String, String>> {
    final  static Logger logger = LoggerFactory.getLogger(ApolloSource.class);
    private volatile boolean isRunning = true;
    private volatile boolean configChangeState = false;
    private    String  defaultKeys = "click";
    private  Config config = null;
    private   String defaultValue = "";
    public ApolloSource(String defaultKeys) {
        this.defaultKeys = defaultKeys;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        System.setProperty("app.id", "ym_big_click");
        System.setProperty("env", "DEV");
        //System.setProperty("apollo.cluster", "dev");
        System.setProperty("apollo.meta", "http://localhost:8080");
        config = ConfigService.getAppConfig();
        config.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                for (String key : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(key);
                    logger.info(String.format(
                            "config info changed - key: %s, oldValue: %s, newValue: %s, changeType: %s",
                            change.getPropertyName(), change.getOldValue(),
                            change.getNewValue(), change.getChangeType()));
                    configChangeState = true;
                }
            }
        });

    }

    @Override
    public void run(SourceContext<Tuple2<String, String>> ctx ) throws Exception {
        while(isRunning ){
            if(configChangeState){
                if(Strings.isNotEmpty(defaultKeys)){
                    Arrays.stream(defaultKeys.split(",")).map(v -> v.trim()).forEach(item ->{
                        String value = config.getProperty(item,defaultValue);
                        ctx.collect(Tuple2.of(item,value));
                        logger.info("run collect:"+value);
                        configChangeState = false;//数据广播后将数据变化状态恢复
                    });
                }

            }else {
                //logger.info("config no change");
            }
          // Thread.sleep(1000L);
        }
    }



    @Override
    public void cancel() {
        isRunning = false;
    }
}
