package io.descoped.container;

import io.descoped.container.module.Bootstrap;
import io.descoped.container.module.DescopedContainer;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.spi.SpiInstanceFactory;
import io.descoped.logger.logback.handler.LogbackBridgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;
import java.util.logging.LogManager;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static {
        Bootstrap.SINGLETON.init();
    }

    private InstanceFactory<DescopedPrimitive> instanceFactory;
    private DescopedContainer<DescopedPrimitive> descopedContainer;

    public Main() {
        log.info("Starting Descoped Container");
    }

    private static void installLogger() {
        LogbackBridgeHandler.installJavaUtilLoggerBridgeHandler();
        LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
    }

    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        installLogger();
        Main main = new Main();
        main.run(() -> {
            long time = System.currentTimeMillis() - now;
            log.info("Server started in {}ms..", time);
            main.waitForKeyPress();
        });
    }

    private void run(Runnable waitFor) {
        instanceFactory = DefaultInstanceFactory.get(SpiInstanceFactory.class);
        descopedContainer = new DescopedContainer<>(instanceFactory);
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.warn("ShutdownHook triggered..");
                stop();
            }));
            descopedContainer.start();
            waitFor.run();
        } finally {
            stop();
        }
    }

    private void waitForKeyPress() {
        try {
            Thread.currentThread().join();
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        if (descopedContainer != null) {
            descopedContainer.stop();
            descopedContainer = null;
            instanceFactory = null;
        }
        log.info("Leaving.. Bye!");
    }

}
