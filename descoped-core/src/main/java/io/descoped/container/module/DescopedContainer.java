package io.descoped.container.module;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.factory.InstanceHandler;
import io.descoped.container.module.factory.LifecycleInstanceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by oranheim on 28/01/2017.
 */
public class DescopedContainer<T extends DescopedPrimitive> {

    private static final Logger log = LoggerFactory.getLogger(DescopedContainer.class);
    private InstanceFactory<T> instanceFactory;

    @Deprecated
    public static <T extends DescopedPrimitive> InstanceHandler<T> findPrimitiveInstance(Class<T> primitiveClass) {
        return DefaultInstanceFactory.findInstance(primitiveClass);
    }

    public DescopedContainer(final InstanceFactory<T> instanceFactory) {
        this.instanceFactory = instanceFactory;
    }

    public int serviceCount() {
        return (instanceFactory == null ? 0 : instanceFactory.instances().size());
    }

    public boolean isRunning() {
        return (instanceFactory != null && !instanceFactory.instances().isEmpty());
    }

    private void load() {
        if (!isRunning()) {
            instanceFactory.load();
        }
    }

    public void start() {
        if (!isRunning()) {
            log.info("Initiating discovery of Descoped Primitives..");
            Map<Class<T>, InstanceHandler<T>> discovered = instanceFactory.load();
            for (Map.Entry<Class<T>, InstanceHandler<T>> primitiveEntry : discovered.entrySet()) {
                InstanceHandler<DescopedPrimitive> primitive = (InstanceHandler<DescopedPrimitive>) primitiveEntry.getValue();
                try {
                    if (!primitive.isRunning()) {
                        int runLevel = primitive.get().getClass().isAnnotationPresent(Priority.class)
                                ? primitive.get().getClass().getAnnotation(Priority.class).value()
                                : PrimitivePriority.CORE;
                        log.trace("Start service: {} [runLevel={}]", primitive.get(), runLevel);
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
            ListIterator<Map.Entry<Class<T>, InstanceHandler<T>>> descendingIterator = instanceFactory.reverseOrder();
            while (descendingIterator.hasPrevious()) {
                Map.Entry<Class<T>, InstanceHandler<T>> primitiveEntry = descendingIterator.previous();
                InstanceHandler<T> primitive = primitiveEntry.getValue();
                try {
                    if (primitive.isRunning()) {
                        int runLevel = primitive.get().getClass().isAnnotationPresent(Priority.class)
                                ? primitive.get().getClass().getAnnotation(Priority.class).value()
                                : PrimitivePriority.CORE;
                        boolean waitFor = primitive.get().getClass().isAnnotationPresent(Primitive.class)
                                ? primitive.get().getClass().getAnnotation(Primitive.class).waitFor()
                                : false;
                        log.trace("Stop service: {} [runLevel={}]", primitive.get(), runLevel);
                        primitive.get().stop();
                        ((LifecycleInstanceHandler)primitive).setRunning(false);
                        // todo: implement waitFor service to complete on interrupt through DescopedPrimitive and -Lifecycle
//                        while (waitFor) {
//                            TimeUnit.MILLISECONDS.sleep(50);
//                        }
                    }
                    instanceFactory.remove(primitiveEntry.getKey());
                } catch (Exception e) {
                    throw new DescopedServerException(e);
                }
            }
            if (!instanceFactory.instances().isEmpty()) {
                throw new IllegalStateException("Error in shutdown of services");
            }
            instanceFactory = null;
        }
    }

    private void release() {
        if (!isRunning()) {
            instanceFactory.instances().clear();
            instanceFactory = null;
        }
    }

}
