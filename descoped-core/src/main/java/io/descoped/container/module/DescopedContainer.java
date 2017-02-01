package io.descoped.container.module;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.ext.DefaultInstanceFactory;
import io.descoped.container.module.ext.InstanceFactory;
import io.descoped.container.module.ext.InstanceHandler;
import io.descoped.container.module.ext.LifecycleInstanceHandler;
import io.descoped.container.module.ext.spi.SpiInstanceFactory;
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
    private static InstanceFactory<DescopedPrimitive> spiInstanceFactory;

    public static boolean isDaemonTestProjectStage() {
        ProjectStage stage = ProjectStageProducer.getInstance().getProjectStage();
        return !DaemonTestProjectStageHolder.DaemonTest.equals(stage);
    }

    public static PrimitiveLifecycle findPrimitiveLifecycleInstance(Class<? extends DescopedPrimitive> primitiveClass) {
//        if (cdiPrimitiveDiscovery == null) throw new IllegalStateException();
//        return (PrimitiveLifecycle) cdiPrimitiveDiscovery.findInstance(primitiveClass);
        return null;
    }

    public static <T extends DescopedPrimitive> InstanceHandler<T> findPrimitiveInstance(Class<T> primitiveClass) {
        return DefaultInstanceFactory.findInstance(primitiveClass);
    }

    public static int serviceCount() {
//        return (cdiPrimitiveDiscovery == null ? 0 : cdiPrimitiveDiscovery.obtain().size());
        return -1;
    }

    public DescopedContainer() {
        spiInstanceFactory = DefaultInstanceFactory.get(SpiInstanceFactory.class);
    }

    public boolean isRunning() {
        return (spiInstanceFactory != null && !spiInstanceFactory.instances().isEmpty());
    }

    public void start() {
        if (!isRunning()) {
            log.info("Initiating discovery of Descoped Primitives..");
            Map<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> discovered = spiInstanceFactory.instances();
            for (Map.Entry<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> primitiveEntry : discovered.entrySet()) {
                InstanceHandler<DescopedPrimitive> primitive = primitiveEntry.getValue();
                try {
                    if (!primitive.isRunning()) {
                        int runLevel = ((PrimitiveLifecycle) primitive).internalClass().isAnnotationPresent(Priority.class)
                                ? ((PrimitiveLifecycle) primitive).internalClass().getAnnotation(Priority.class).value()
                                : PrimitivePriority.CORE;
                        log.trace("Start service: {} [runLevel={}]", primitive, runLevel);
                        primitive.get().start();
                        ((LifecycleInstanceHandler)primitive).setRunning(true);
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
            ListIterator<Map.Entry<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>>> descendingIterator = spiInstanceFactory.reverseOrder();
            while (descendingIterator.hasPrevious()) {
                Map.Entry<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> primitiveEntry = descendingIterator.previous();
                InstanceHandler<DescopedPrimitive> primitive = primitiveEntry.getValue();
                try {
                    if (primitive.isRunning()) {
                        int runLevel = ((PrimitiveLifecycle) primitive).internalClass().isAnnotationPresent(Priority.class)
                                ? ((PrimitiveLifecycle) primitive).internalClass().getAnnotation(Priority.class).value()
                                : PrimitivePriority.CORE;
                        boolean waitFor = ((PrimitiveLifecycle) primitive).internalClass().isAnnotationPresent(Primitive.class)
                                ? ((PrimitiveLifecycle) primitive).internalClass().getAnnotation(Primitive.class).waitFor()
                                : false;
                        log.trace("Stop service: {} [runLevel={}]", primitive, runLevel);
                        primitive.get().stop();
                        ((LifecycleInstanceHandler)primitive).setRunning(false);
                        // todo: implement waitFor service to complete on interrupt through DescopedPrimitive and -Lifecycle
//                        while (waitFor) {
//                            TimeUnit.MILLISECONDS.sleep(50);
//                        }
                    }
                    spiInstanceFactory.remove(primitiveEntry.getKey());
                } catch (Exception e) {
                    throw new DescopedServerException(e);
                }
            }
            if (!spiInstanceFactory.instances().isEmpty()) {
                throw new IllegalStateException("Error in shutdown of services");
            }
            spiInstanceFactory = null;
        }
    }

}
