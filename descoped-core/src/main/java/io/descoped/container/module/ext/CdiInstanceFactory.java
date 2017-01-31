package io.descoped.container.module.ext;

import io.descoped.container.module.PrimitiveModule;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by oranheim on 30/01/2017.
 * <p>
 * create instances from resolve all beans
 */
public class CdiInstanceFactory<T> implements InstanceFactory<T> {

    private final Class<T> factoryClass;
    private List<Class<?>> discoveredClasses = null;
    private Map<Class<T>, Instance<T>> instances = new LinkedHashMap<>();

    // don't pass instance param here, but resolve all beans and add then to instanceList
    public CdiInstanceFactory(final Class<T> factoryClass) {
        this.factoryClass = factoryClass;
    }

    @Override
    public List<Class<?>> discover() {
        if (discoveredClasses != null) return discoveredClasses;

        BeanManager beanManager = CDI.current().getBeanManager();
        Set<Bean<?>> beans = beanManager.getBeans(factoryClass);
        List<Class<?>> acceptClasses = new ArrayList<>();
        List<Class<?>> classes = beans.stream().map(Bean::getBeanClass).collect(Collectors.toList());
        for(Class<?> clazz : classes) {
            if (accept(clazz)) {
                acceptClasses.add(clazz);
            }
        }
        return acceptClasses;
    }

    @Override
    public boolean accept(Class<?> clazz) {
        boolean isDescopedModule = clazz.isAnnotationPresent(PrimitiveModule.class);
        return !isDescopedModule;
    }

    @Override
    public Map<Class<T>, Instance<T>> instances() {
        if (!instances.isEmpty()) return instances;

        List<Class<?>> classes = discover();
        for(Class<?> clazz : classes) {
            T bean = (T) CDI.current().select(clazz).get();
            create((Class<T>) clazz, bean);
        }

        return instances;
    }

    @Override
    public Instance<T> create(Class<T> clazz, T instance) {
        Instance<T> instanceHandler = new CdiInstanceHandle(instance);
        instances.put(clazz, instanceHandler);
        return instanceHandler;
    }

}
