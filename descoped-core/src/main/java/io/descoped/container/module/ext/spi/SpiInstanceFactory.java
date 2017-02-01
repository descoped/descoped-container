package io.descoped.container.module.ext.spi;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import io.descoped.container.module.ext.BaseInstanceFactory;
import io.descoped.container.module.ext.Instance;
import io.descoped.container.module.ext.InstanceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by oranheim on 31/01/2017.
 */
public class SpiInstanceFactory<T extends DescopedPrimitive> extends BaseInstanceFactory<T> {

    private final static Logger log = LoggerFactory.getLogger(SpiInstanceFactory.class);
    private volatile Map<Class<T>, T> weakInstanceMap = new WeakHashMap<>();

    public SpiInstanceFactory(final Class<T> factoryClass) {
        super(factoryClass);
    }

    private ServiceLoader<? extends T> serviceLoader() {
        ServiceLoader<? extends T> modules = ServiceLoader.load(factoryClass);
        return modules;
    }

    @Override
    protected List<Class<T>> discover() {
        List<Class<T>> classes = new ArrayList<>();
        ServiceLoader<? extends T> loader = serviceLoader();
        for(Iterator<? extends T> it = loader.iterator(); it.hasNext(); ) {
            T module = it.next();
            Class<T> moduleClass = (Class<T>) module.getClass();
            if (accept(moduleClass)) {
                classes.add(moduleClass);
                weakInstanceMap.put(moduleClass, module);
            }
        }
        return classes;
    }

    @Override
    protected boolean accept(Class<T> clazz) {
        boolean isDescopedModule = clazz.isAnnotationPresent(PrimitiveModule.class);
        if (!isDescopedModule) log.trace("Skip Class: {}", clazz);
        return isDescopedModule;
    }

    @Override
    protected InstanceHandler<T> create(Class<T> clazz, Object instance) {
        InstanceHandler<T> instanceHandler = new Instance<>((T) instance);
        return instanceHandler;
    }

    @Override
    protected Object get(Class<T> primitive) {
        return weakInstanceMap.get(primitive);
    }

}
