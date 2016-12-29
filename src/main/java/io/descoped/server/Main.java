package io.descoped.server;

import io.descoped.server.container.PreStartContainer;
import io.descoped.server.container.ServerContainer;
import io.descoped.server.container.UndertowContainer;
import io.descoped.server.support.ContainerType;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.util.ProjectStageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.enterprise.event.Observes;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private ContainerType containerType = ContainerType.UNDERTOW;
    private ServerContainer server;

    public Main() {
    }

    public ServerContainer createContainer(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.newInstance();
            return (ServerContainer) instance;
        } catch (ClassNotFoundException e) {
            log.error("Unable to load '{}'!", e);
        } catch (IllegalAccessException e) {
            log.error("Unable to load '{}'!", e);
        } catch (InstantiationException e) {
            log.error("Unable to load '{}'!", e);
        }
        return null;
    }

    public void start() {
        if (server == null) {
            server = (containerType.equals(ContainerType.UNDERTOW) ? new UndertowContainer() : createContainer("io.descoped.server.containerGrizzlyContainer") );
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

    public int getPort() {
        return (server != null ? server.getPort() : 8080);
    }

    public boolean isRunning() {
        return (server != null && server.isRunning());
    }

    public boolean isStopped() {
        return (server == null || server.isStopped());
    }

    public void shutdown() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            System.exit(-1);
        }
        if (server != null) server.shutdown();
        server = null;
    }

    private boolean isNotRestDeploymentForDeamonTesting() {
        ProjectStage stage = ProjectStageProducer.getInstance().getProjectStage();
        return !io.descoped.server.deployment.RestDeploymentProjectStageHolder.RestDeployment.equals(stage);
    }

    public void observeDeploymentHandlers(@Observes PreStartContainer event) {
        if (isNotRestDeploymentForDeamonTesting()) return;
        UndertowContainer container = (UndertowContainer) event.container();
        container.deployJaxRsResourceConfig();
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
            cdiContainer.getContextControl().startContexts();
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
