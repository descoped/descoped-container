package io.descoped.container;

import io.descoped.container.core.PreStartContainer;
import io.descoped.container.core.ServerContainer;
import io.descoped.container.core.UndertowContainer;
import io.descoped.container.deployment.DaemonTestProjectStageHolder;
import io.descoped.container.support.WebServerLiteral;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.util.ProjectStageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private ServerContainer serverContainer;

    public Main() {
    }

    private static void installLogger() {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
    }

    public boolean isRunning() {
        return (serverContainer == null ? false : serverContainer.isRunning());
    }

    public int getPort() {
        return (serverContainer == null ? 8080 : serverContainer.getPort());
    }

    private void run(Runnable waitFor) {
        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        try {
            cdiContainer.boot();
            cdiContainer.getContextControl().startContexts();
            try {
                start();
                waitFor.run();
            } finally {
                stop();
            }
        } finally {
            cdiContainer.shutdown();
        }
    }

    public void start() {
        if (serverContainer == null) {
            serverContainer = CDI.current().select(ServerContainer.class, WebServerLiteral.DEFAULT).get();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    log.debug("ShutdownHook triggered..");
                    if (serverContainer != null) {
                        serverContainer.shutdown();
                        serverContainer = null;
                    }
                }
            });

            serverContainer.start();
        }
    }

    private void waitForKeyPress() {
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (serverContainer != null) serverContainer.shutdown();
        serverContainer = null;
    }

    private boolean isDaemonTestProjectStage() {
        ProjectStage stage = ProjectStageProducer.getInstance().getProjectStage();
        return !DaemonTestProjectStageHolder.DaemonTest.equals(stage);
    }

    private void observeDeploymentHandlers(@Observes PreStartContainer event) {
        if (isDaemonTestProjectStage()) return;
        UndertowContainer container = (UndertowContainer) event.container();
        container.deployJaxRsResourceConfig();
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

}
