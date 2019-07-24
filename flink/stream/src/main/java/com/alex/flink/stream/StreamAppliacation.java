package com.alex.flink.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamAppliacation {
    final  static  Logger logger = LoggerFactory.getLogger(StreamAppliacation.class);
    public static void main(String[] args) {
        logger.info("test log");
        logger.error("test log");
    }
}
