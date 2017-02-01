package io.descoped.container.module.factory;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.cdi.CdiInstanceFactory;
import io.descoped.container.module.spi.SpiInstanceFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by oranheim on 01/02/2017.
 */
final public class DefaultInstanceFactory {

    private static final Map<Class<? extends InstanceFactory>, InstanceFactory<DescopedPrimitive>> INSTANCES = new ConcurrentHashMap<>();

    private static final void register(Class<? extends InstanceFactory> factoryClass, InstanceFactory<DescopedPrimitive> factory) {
        INSTANCES.put(factoryClass, factory);
    }

    private static final void init() {
        register(SpiInstanceFactory.class, new SpiInstanceFactory<>(DescopedPrimitive.class));
        register(CdiInstanceFactory.class, new CdiInstanceFactory<>(DescopedPrimitive.class));
    }

    static {
        init();
    }

    public static final InstanceFactory<DescopedPrimitive> get(Class<? extends InstanceFactory> factoryClass) {
        if (INSTANCES.isEmpty()) {
            init();
        }
        return INSTANCES.get(factoryClass);
    }

    public static final <T extends DescopedPrimitive> InstanceHandler<T> findInstance(Class<T> clazz) {
        for(Map.Entry<Class<? extends InstanceFactory>, InstanceFactory<DescopedPrimitive>> entry : INSTANCES.entrySet()) {
            InstanceHandler<DescopedPrimitive> instanceHandler = entry.getValue().find((Class<DescopedPrimitive>) clazz);
            if (instanceHandler != null) {
                return (InstanceHandler<T>) instanceHandler;
            }
        }
        return null;
    }

}
