package io.descoped.container.module;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.stage.DaemonTestProjectStageHolder;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.util.ProjectStageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by oranheim on 28/01/2017.
 */
public class DescopedContainer {

    private static final Logger log = LoggerFactory.getLogger(DescopedContainer.class);

    private static PrimitiveDiscovery discovery;

    public static boolean isDaemonTestProjectStage() {
        ProjectStage stage = ProjectStageProducer.getInstance().getProjectStage();
        return !DaemonTestProjectStageHolder.DaemonTest.equals(stage);
    }

    public static PrimitiveLifecycle findPrimitiveLifecycleInstance(Class<? extends DescopedPrimitive> primitiveClass) {
        if (discovery == null) throw new IllegalStateException();
        return (PrimitiveLifecycle) discovery.findInstance(primitiveClass);
    }

    public static <T extends DescopedPrimitive> T findPrimitiveInstance(Class<T> primitiveClass) {
        T instance = (T) findPrimitiveLifecycleInstance(primitiveClass).getDelegate();
        return instance;
    }

    public static int serviceCount() {
        return (discovery == null ? 0 : discovery.obtain().size());
    }

    public DescopedContainer() {
    }

    public boolean isRunning() {
        return (discovery != null && !discovery.isEmpty());
    }

    public void start() {
        if (!isRunning()) {
            log.info("Initiating discovery of Descoped Primitives..");
            discovery = new PrimitiveDiscovery();
            Map<Class<DescopedPrimitive>, DescopedPrimitive> discovered = discovery.obtain();
            for (Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive> primitiveEntry : discovered.entrySet()) {
                DescopedPrimitive primitive = primitiveEntry.getValue();
                try {
                    if (!primitive.isRunning()) {
                        int runLevel = ((PrimitiveLifecycle) primitive).internalClass().isAnnotationPresent(Priority.class)
                                ? ((PrimitiveLifecycle) primitive).internalClass().getAnnotation(Priority.class).value()
                                : PrimitivePriority.CORE;
                        log.trace("Start service: {} [runLevel={}]", primitive, runLevel);
                        primitive.start();
                    }
                } catch (Exception e) {
                    throw new DescopedServerException(e);
                }
            }
            log.info("Started {} primitives", discovered.size());
        }
    }

    public void stop() {
        if (isRunning()) {
            log.info("Releasing all Descoped Primitives..");
            ListIterator<Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive>> descendingIterator = discovery.reverseOrder();
            while (descendingIterator.hasPrevious()) {
                Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive> primitiveEntry = descendingIterator.previous();
                DescopedPrimitive primitive = primitiveEntry.getValue();
                try {
                    if (primitive.isRunning()) {
                        int runLevel = ((PrimitiveLifecycle) primitive).internalClass().isAnnotationPresent(Priority.class)
                                ? ((PrimitiveLifecycle) primitive).internalClass().getAnnotation(Priority.class).value()
                                : PrimitivePriority.CORE;
                        boolean waitFor = ((PrimitiveLifecycle) primitive).internalClass().isAnnotationPresent(Primitive.class)
                                ? ((PrimitiveLifecycle) primitive).internalClass().getAnnotation(Primitive.class).waitFor()
                                : false;
                        log.trace("Stop service: {} [runLevel={}]", primitive, runLevel);
                        primitive.stop();
                        // todo: implement waitFor service to complete on interrupt through DescopedPrimitive and -Lifecycle
//                        while (waitFor) {
//                            TimeUnit.MILLISECONDS.sleep(50);
//                        }
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


}
