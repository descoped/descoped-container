package io.descoped.container;

import io.descoped.container.module.Bootstrap;
import io.descoped.container.module.DescopedContainer;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static {
        Bootstrap.SINGLETON.init();
    }

    private DescopedContainer descopedContainer;

    public Main() {
        log.info("Starting Descoped Container");
        descopedContainer = new DescopedContainer();
    }

    private static void installLogger() {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
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
        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        try {
            cdiContainer.boot();
            cdiContainer.getContextControl().startContext(ApplicationScoped.class);
            try {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        log.debug("ShutdownHook triggered..");
                        if (descopedContainer != null) {
                            descopedContainer.stop();
                            descopedContainer = null;
                        }
                    }
                });
                descopedContainer.start();
                waitFor.run();
            } finally {
                if (descopedContainer != null) descopedContainer.stop();
            }
            cdiContainer.getContextControl().stopContext(ApplicationScoped.class);
        } finally {
            cdiContainer.shutdown();
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

}
