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

    private final Class<T> typedClass;
    private Map<Class<T>, Instance<T>> instances = new LinkedHashMap<>();

    // don't pass instance param here, but resolve all beans and add then to instanceList
    public CdiInstanceFactory(final Class<T> typedClass) {
        this.typedClass = typedClass;
    }

    private List<Class<?>> discover() {
        BeanManager beanManager = CDI.current().getBeanManager();
        Set<Bean<?>> beans = beanManager.getBeans(typedClass);
        List<Class<?>> clazzes = new ArrayList<>();
        List<Class<?>> classes = beans.stream().map(Bean::getBeanClass).collect(Collectors.toList());
        for(Class<?> clazz : classes) {
            if (accept(clazz)) {
                clazzes.add(clazz);
            }
        }
        return clazzes;
    }

    public Map<Class<T>, Instance<T>> instances() {
        if (!instances.isEmpty()) return instances;

//            Map<Class<DescopedPrimitive>, Integer> primitives = discover();
//            for (Map.Entry<Class<DescopedPrimitive>, Integer> primitive : primitives.entrySet()) {
//                DescopedPrimitive primitiveInstance = getClass(primitive.getKey());
//                DescopedPrimitive lifecycle = new PrimitiveLifecycle(primitiveInstance);
//                try {
//                    lifecycle.init();
//                    cache.put(primitive.getKey(), lifecycle);
//                } catch (Exception e) {
//                    throw new IllegalStateException();
//                }
//            }
        return null;

    }

    @Override
    public boolean accept(Class<?> clazz) {
        boolean isDescopedModule = clazz.isAnnotationPresent(PrimitiveModule.class);
        return !isDescopedModule;
    }
}
