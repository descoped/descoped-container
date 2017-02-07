package io.descoped.container.module.factory;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.module.DescopedPrimitive;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by oranheim on 01/02/2017.
 */
final public class DefaultInstanceFactory {

    private static final Map<Class<? extends InstanceFactory>, InstanceFactory<DescopedPrimitive>> INSTANCES = new ConcurrentHashMap<>();

    public static final void register(Class<? extends InstanceFactory> factoryClass, InstanceFactory<DescopedPrimitive> factory) {
        INSTANCES.put(factoryClass, factory);
    }

    public static final InstanceFactory<DescopedPrimitive> get(Class<? extends InstanceFactory> factoryClass) {
        if (!INSTANCES.containsKey(factoryClass)) {
            try {
                Method staticFactoryInitMethod = factoryClass.getDeclaredMethod("init", null);
                staticFactoryInitMethod.invoke(null, null);
                if (!INSTANCES.containsKey(factoryClass)) {
                    throw new DescopedServerException("Something went wrong in " + factoryClass + ".init()! Make sure DefaultInstanceFactory.register(factoryClass, factory) is invoked from this method!");
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new DescopedServerException("Unable to initialize: " + factoryClass);
            }
        }
        return INSTANCES.get(factoryClass);
    }

    public static final <T extends DescopedPrimitive> InstanceHandler<T> findInstance(Class<T> clazz) {
        for (Map.Entry<Class<? extends InstanceFactory>, InstanceFactory<DescopedPrimitive>> entry : INSTANCES.entrySet()) {
            InstanceHandler<DescopedPrimitive> instanceHandler = entry.getValue().find((Class<DescopedPrimitive>) clazz);
            if (instanceHandler != null) {
                return (InstanceHandler<T>) instanceHandler;
            }
        }
        return null;
    }

}
