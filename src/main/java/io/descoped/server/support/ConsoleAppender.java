package io.descoped.server.support;

import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.LogManager;

/**
 * Created by oranheim on 22/12/2016.
 *
 * This class is only used by @TestControl(logHandler = ConsoleAppender.class)
 */
public class ConsoleAppender extends java.util.logging.ConsoleHandler {

    public ConsoleAppender() {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        //LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
    }

}
