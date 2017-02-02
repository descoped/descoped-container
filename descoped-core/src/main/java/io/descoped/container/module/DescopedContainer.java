package io.descoped.container.module;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.factory.InstanceHandler;
import io.descoped.container.module.factory.LifecycleInstanceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by oranheim on 28/01/2017.
 */
public class DescopedContainer<T extends DescopedPrimitive> {

    private Logger log;
    private InstanceFactory<T> instanceFactory;

    public DescopedContainer(final InstanceFactory<T> instanceFactory) {
        log = LoggerFactory.getLogger(DescopedContainer.class.getName() + "::" + instanceFactory.getClass().getSimpleName());
        this.instanceFactory = instanceFactory;
    }

    @Deprecated
    public static <T extends DescopedPrimitive> InstanceHandler<T> findPrimitiveInstance(Class<T> primitiveClass) {
        return DefaultInstanceFactory.findInstance(primitiveClass);
    }

    public int serviceCount() {
        return (instanceFactory == null ? 0 : instanceFactory.instances().size());
    }

    public boolean isRunning() {
        for (Map.Entry<Class<T>, InstanceHandler<T>> entry : instanceFactory.instances().entrySet()) {
            if (entry.getValue().isRunning()) {
                return true;
            }
        }
        return false;
    }

    private String factoryName() {
        String factoryName = instanceFactory.name();
        if ("SPI".equals(factoryName)) factoryName = "SPI Module";
        if ("CDI".equals(factoryName)) factoryName = "CDI Extension";
        return factoryName;
    }


    public void start() {
        if (!isRunning()) {
            log.info("Initiating discovery of Descoped {} Primitives..", factoryName());
            Map<Class<T>, InstanceHandler<T>> discovered = instanceFactory.load();
            for (Map.Entry<Class<T>, InstanceHandler<T>> primitiveEntry : discovered.entrySet()) {
                InstanceHandler<DescopedPrimitive> primitive = (InstanceHandler<DescopedPrimitive>) primitiveEntry.getValue();
                try {
                    DescopedPrimitive primitiveInstance = primitive.get();
                    if (!primitive.isRunning()) {
                        Class<DescopedPrimitive> annoClass = primitive.getType();
                        int runLevel = annoClass.isAnnotationPresent(Priority.class)
                                ? annoClass.getAnnotation(Priority.class).value()
                                : PrimitivePriority.CORE;

                        log.trace("Invoke {} {}.start() [runLevel={}]", factoryName(), annoClass, runLevel);
                        primitiveInstance.start();
                        log.trace("Invoked {} {}.start() [runLevel={}]", factoryName(), annoClass, runLevel);
                        ((LifecycleInstanceHandler) primitive).setRunning(true);
                    }
                } catch (Exception e) {
                    throw new DescopedServerException(e);
                }
            }
            log.info("Started {} {} primitives", discovered.size(), factoryName());
        }
    }

    public void stop() {
        if (isRunning()) {
            ListIterator<Map.Entry<Class<T>, InstanceHandler<T>>> descendingIterator = instanceFactory.reverseOrder();
            log.info("Releasing {} Descoped {} Primitives..", Arrays.asList(descendingIterator).size(), factoryName());
            int count = 0;
            while (descendingIterator.hasPrevious()) {
                Map.Entry<Class<T>, InstanceHandler<T>> primitiveEntry = descendingIterator.previous();
                InstanceHandler<T> primitive = primitiveEntry.getValue();
                try {
                    DescopedPrimitive primitiveInstance = primitive.get();
                    if (primitive.isRunning()) {
                        Class<T> annoClass = primitive.getType();

                        int runLevel = annoClass.isAnnotationPresent(Priority.class)
                                ? annoClass.getAnnotation(Priority.class).value()
                                : PrimitivePriority.CORE;
                        boolean waitFor = annoClass.isAnnotationPresent(Primitive.class)
                                ? annoClass.getAnnotation(Primitive.class).waitFor()
                                : false;

                        log.trace("Invoke {} {}.stop() [runLevel={}]", factoryName(), annoClass, runLevel);
                        primitiveInstance.stop();
                        log.trace("Invoked {} {}.stop() [runLevel={}]", factoryName(), annoClass, runLevel);
                        ((LifecycleInstanceHandler) primitive).setRunning(false);
                        // todo: implement waitFor service to complete on interrupt through DescopedPrimitive and -Lifecycle
//                        while (waitFor) {
//                            TimeUnit.MILLISECONDS.sleep(50);
//                        }
//                        Thread.sleep(500);
                        log.trace("Removing instance: {}", primitiveEntry.getKey());
                        instanceFactory.remove(primitiveEntry.getKey());
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw new DescopedServerException(e);
                    log.error("Error during shutdown of DescopedContainer!", e);
                }
                count++;
            }
            log.info("Stopped {} {} primitives", count, factoryName());
            if (!instanceFactory.instances().isEmpty()) {
                throw new IllegalStateException("Error in shutdown of services");
            }
            instanceFactory = null;
        }
    }

}
