package io.descoped.server;

import io.descoped.server.container.GrizzlyContainer;
import io.descoped.server.container.ServerContainer;
import io.descoped.server.container.UndertowContainer;
import io.descoped.server.support.ContainerType;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;

public class Main extends ServerContainer {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private ContainerType containerType = ContainerType.UNDERTOW;
    private ServerContainer server;

    public Main() {
    }

    public void start() {
        if (server == null) {
            server = (containerType.equals(ContainerType.UNDERTOW) ? new UndertowContainer(this) : new GrizzlyContainer(this));
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    log.debug("ShutdownHook triggered..");
                    if (server != null) {
                        server.shutdown();
                        server = null;
                    }
                }
            });
            server.start();
        }
    }

    public boolean isRunning() {
        return (server != null && server.isRunning());
    }

    public boolean isStopped() {
        return (server == null || server.isStopped());
    }

    public void shutdown() {
        if (server != null) server.shutdown();
        server = null;
    }

    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        {
            LogManager.getLogManager().reset();
            SLF4JBridgeHandler.removeHandlersForRootLogger();
            SLF4JBridgeHandler.install();
            LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
        }

        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        try {
            cdiContainer.boot();
            Main main = new Main();
            try {
                main.start();
                try {
                    long time = System.currentTimeMillis() - now;
                    log.info("Server started in {}ms..", time);
                    System.in.read();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } finally {
                main.shutdown();
            }
        } finally {
            cdiContainer.shutdown();
        }
    }

}
