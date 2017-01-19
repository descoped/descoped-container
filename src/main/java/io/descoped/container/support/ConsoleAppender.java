package io.descoped.container.support;

import io.descoped.container.util.CommonUtil;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.File;
import java.util.logging.LogManager;

/**
 * Created by oranheim on 22/12/2016.
 *
 * This class is only used by @TestControl(logHandler = ConsoleAppender.class)
 */
public class ConsoleAppender extends java.util.logging.ConsoleHandler {

    public ConsoleAppender() {
        if (new File(CommonUtil.currentPath() + "/src/main/resources/logging.properties").exists()) {
            System.setProperty("java.util.logging.config", CommonUtil.currentPath() + "/src/main/resources/logging.properties");
            System.out.println("---------------------> set logging props: " + System.getProperty("java.util.logging.config"));
        }
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        //LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
    }

}
