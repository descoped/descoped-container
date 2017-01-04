package io.descoped.container.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by oranheim on 04/01/2017.
 */
public class CommonUtil {

    private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);

    public static void info(String msg, Object... values) {
        log.info(msg, values);
    }

    public static void debug(String msg, Object... values) {
        log.debug(msg, values);
    }

    public static void trace(String msg, Object... values) {
        log.trace(msg, values);
    }

    public static void warn(String msg, Object... values) {
        log.warn(msg, values);
    }

    public static void error(String msg, Object... values) {
        log.error(msg, values);
    }


}
