package io.descoped.container.module;

import org.jboss.weld.bean.proxy.ProxyObject;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by oranheim on 27/01/2017.
 */
@ApplicationScoped
public class PrimitiveDiscovery {

    private Map<Class<DescopedPrimitive>, DescopedPrimitive> cache = new LinkedHashMap<>();

    private Map<Class<DescopedPrimitive>, Integer> sortMapByPriority(Map<Class<DescopedPrimitive>, Integer> map) {
        return map.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Map<Class<DescopedPrimitive>, Integer> discover() {
        BeanManager beanManager = CDI.current().getBeanManager();
        Set<Bean<?>> beans = beanManager.getBeans(DescopedPrimitive.class);
        List<Class<?>> classes = beans.stream().map(Bean::getBeanClass).collect(Collectors.toList());
        Map<Class<DescopedPrimitive>, Integer> map = new LinkedHashMap<>();
        for (Class<?> clazz : classes) {
            Integer priority = clazz.isAnnotationPresent(Priority.class) ? clazz.getAnnotation(Priority.class).value() : PrimitivePriority.CORE;
            map.put((Class<DescopedPrimitive>) clazz, priority);
        }
        return sortMapByPriority(map);
    }

    public Map<Class<DescopedPrimitive>, DescopedPrimitive> obtain() {
        if (cache.isEmpty()) {
            Map<Class<DescopedPrimitive>, Integer> primitives = discover();
            for (Map.Entry<Class<DescopedPrimitive>, Integer> primitive : primitives.entrySet()) {
                DescopedPrimitive primitiveInstance = CDI.current().select(primitive.getKey()).get();
                cache.put(primitive.getKey(), new PrimitiveLifecycle(primitiveInstance));
            }
        }
        return cache;
    }

    public ListIterator<Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive>> reverseOrder() {
        return new ArrayList<>(cache.entrySet()).listIterator(cache.size());
    }

    public DescopedPrimitive findInstance(Class<? extends DescopedPrimitive> primitiveClass) {
        primitiveClass = (ProxyObject.class.isAssignableFrom(primitiveClass) ? (Class<? extends DescopedPrimitive>) primitiveClass.getSuperclass() : primitiveClass);
        return cache.get(primitiveClass);
    }

    public void removePrimitive(Class<DescopedPrimitive> primitiveClass) {
        cache.remove(primitiveClass);
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }
}
