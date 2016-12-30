package org.apache.deltaspike.servlet.impl.produce;

/**
 * Created by oranheim on 30/12/2016.
 */
public class ServletContextHolderHelper {

    public static void release() {
        ServletContextHolder.release();
    }

}
