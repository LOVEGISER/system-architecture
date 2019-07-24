package com.alex.flink;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;


public class Test {
    public static void main(String[] args) {
        try {
            System.setProperty("app.id", "ym_big_click");
            System.setProperty("env", "DEV");
           // System.setProperty("apollo.cluster", "dev");
            System.setProperty("apollo.meta", "http://localhost:8080");
            Config config = ConfigService.getAppConfig();
            config.addChangeListener(new ConfigChangeListener() {
                @Override
                public void onChange(ConfigChangeEvent changeEvent) {
                    for (String key : changeEvent.changedKeys()) {
                        ConfigChange change = changeEvent.getChange(key);
                        System.out.println(String.format(
                                "Found change - key: %s, oldValue: %s, newValue: %s, changeType: %s",
                                change.getPropertyName(), change.getOldValue(),
                                change.getNewValue(), change.getChangeType()));
                    }
                }
            });
        while (true) {
            System.out.println((System.currentTimeMillis() % 3) == 0);
            System.out.println((10 % 3));
            Thread.sleep(1001l);
        }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
