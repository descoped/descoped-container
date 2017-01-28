package io.descoped.container;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.*;
import io.descoped.container.stage.DaemonTestProjectStageHolder;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.util.ProjectStageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static {
        Bootstrap.SINGLETON.init();
    }

    private static PrimitiveDiscovery discovery;

    public Main() {
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

    public boolean isRunning() {
        return (discovery != null && !discovery.isEmpty());
    }

//    public int getPort() {
//        return (serverContainer == null ? 8080 : serverContainer.getPort());
//    }

    public static DescopedPrimitive findServiceInstance(Class<? extends DescopedPrimitive> primitiveClass) {
        if (discovery == null) throw new IllegalStateException();
        return discovery.findInstance(primitiveClass);
    }

    public static int serviceCount() {
        return (discovery == null ? 0 : discovery.obtain().size());
    }

    private void run(Runnable waitFor) {
        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        try {
            cdiContainer.boot();
            cdiContainer.getContextControl().startContext(ApplicationScoped.class);
            try {
                start();
                waitFor.run();
            } finally {
                stop();
            }
            cdiContainer.getContextControl().stopContext(ApplicationScoped.class);
        } finally {
            cdiContainer.shutdown();
        }
    }

    public void start() {
        if (!isRunning()) {
            discovery = new PrimitiveDiscovery();
            Map<Class<DescopedPrimitive>, DescopedPrimitive> discovered = discovery.obtain();
            for (Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive> primitiveEntry : discovered.entrySet()) {
                DescopedPrimitive primitive = primitiveEntry.getValue();
                try {
                    if (!DescopedPrimitive.isRunning(primitive)) {
                        int runLevel = ((PrimitiveLifecycle)primitive).internalClass().isAnnotationPresent(Priority.class)
                            ? ((PrimitiveLifecycle)primitive).internalClass().getAnnotation(Priority.class).value()
                            : PrimitivePriority.CORE;
                        log.trace("Start service: {} [runLevel={}]", primitive, runLevel);
                        primitive.init();
                        primitive.start();
                    }
                } catch (Exception e) {
                    throw new DescopedServerException(e);
                }
            }
        }
    }

//    public void start() {
//        if (serverContainer == null) {
//            serverContainer = CDI.current().select(ServerContainer.class, WebServerLiteral.CORE).get();
//
//            Runtime.getRuntime().addShutdownHook(new Thread() {
//                public void run() {
//                    log.debug("ShutdownHook triggered..");
//                    if (serverContainer != null) {
//                        if (!serverContainer.isStopped()) {
//                            serverContainer.shutdown();
//                        }
//                        serverContainer = null;
//                    }
//                }
//            });
//
//            serverContainer.start();
//        }
//    }

    public void stop() {
        if (isRunning()) {
            ListIterator<Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive>> descendingIterator = discovery.reverseOrder();
            while (descendingIterator.hasPrevious()) {
                Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive> primitiveEntry = descendingIterator.previous();
                DescopedPrimitive primitive = primitiveEntry.getValue();
                try {
                    if (DescopedPrimitive.isRunning(primitive)) {
                        int runLevel = ((PrimitiveLifecycle)primitive).internalClass().isAnnotationPresent(Priority.class)
                            ? ((PrimitiveLifecycle)primitive).internalClass().getAnnotation(Priority.class).value()
                            : PrimitivePriority.CORE;
                        boolean waitFor = ((PrimitiveLifecycle)primitive).internalClass().isAnnotationPresent(Primitive.class)
                            ? ((PrimitiveLifecycle)primitive).internalClass().getAnnotation(Primitive.class).waitFor()
                            : false;
                        log.trace("Stop service: {} [runLevel={}]", primitive, runLevel);
                        primitive.stop();
//                        while (waitFor) {
//                            TimeUnit.MILLISECONDS.sleep(50);
//                        }
                        primitive.destroy();
                    }
                    discovery.removePrimitive(primitiveEntry.getKey());
                } catch (Exception e) {
                    throw new DescopedServerException(e);
                }
            }
            if (!discovery.isEmpty()) {
                throw new IllegalStateException("Error in shutdown of services");
            }
            discovery = null;
        }
    }

//    public void stop() {
//        if (serverContainer != null) serverContainer.shutdown();
//        serverContainer = null;
//    }

    private void waitForKeyPress() {
        try {
            Thread.currentThread().join();
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void observeDeploymentHandlers(@Observes PreStartContainer event) {
//        if (isDaemonTestProjectStage()) return;
//        UndertowContainer container = (UndertowContainer) event.container();
//        if (container.getDeployments().isEmpty()) {
//            container.deployJaxRsResourceConfig();
//        }
//    }

    public static boolean isDaemonTestProjectStage() {
        ProjectStage stage = ProjectStageProducer.getInstance().getProjectStage();
        return !DaemonTestProjectStageHolder.DaemonTest.equals(stage);
    }

}
