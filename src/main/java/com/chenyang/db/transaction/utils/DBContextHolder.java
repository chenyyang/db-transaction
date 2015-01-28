package com.chenyang.db.transaction.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBContextHolder {

    // 保证线程间不受影响
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    private static Logger logger = LoggerFactory.getLogger(DBContextHolder.class);

    public static void clearCatelog() {
        contextHolder.remove();
    }

    public static String getCateloge() {
        logger.debug(" get catelog : {}", DBContextHolder.contextHolder.get());
        return contextHolder.get();
    }

    public static void setCatelog(String catelog) {
        logger.debug(" set catelog : {}", catelog);
        contextHolder.set(catelog);
    }
}
