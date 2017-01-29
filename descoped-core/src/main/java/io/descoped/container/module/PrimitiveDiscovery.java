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

    private static PrimitiveDiscovery instance = null;

    protected boolean useSPILoader = false;

    private Map<Class<DescopedPrimitive>, DescopedPrimitive> cache = new LinkedHashMap<>();

    private PrimitiveDiscovery() {
    }

    private List<Class<?>> discoverCdiClasses() {
        BeanManager beanManager = CDI.current().getBeanManager();
        Set<Bean<?>> beans = beanManager.getBeans(DescopedPrimitive.class);
        List<Class<?>> clazzes = new ArrayList<>();
        List<Class<?>> classes = beans.stream().map(Bean::getBeanClass).collect(Collectors.toList());
        classes.forEach(clazz -> {
            boolean isDescopedModule = clazz.isAnnotationPresent(PrimitiveModule.class);
            if (!isDescopedModule) {
                clazzes.add(clazz);
            }
        });
        return clazzes;
    }

    private <T extends DescopedPrimitive> T getCdiClass(Class<T> clazz) {
        return CDI.current().select(clazz).get();
    }

    private List<Class<?>> discoverModuleClasses() {
        List<Class<?>> classes = ModuleLoader.discover();
        return classes;
    }

    private <T extends DescopedPrimitive> T getModuleClass(Class<T> clazz) {
        return ModuleLoader.get(clazz);
    }

    private List<Class<?>> discoverClasses() {
        return (useSPILoader ? discoverModuleClasses() : discoverCdiClasses());
    }

    private <T extends DescopedPrimitive> T getClass(Class<T> clazz) {
        return (useSPILoader ? getModuleClass(clazz) : getCdiClass(clazz));
    }

    private Map<Class<DescopedPrimitive>, Integer> sortMapByPriority(Map<Class<DescopedPrimitive>, Integer> map) {
        return map.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Map<Class<DescopedPrimitive>, Integer> discover() {
        List<Class<?>> classes = discoverClasses();

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
                DescopedPrimitive primitiveInstance = getClass(primitive.getKey());
                DescopedPrimitive lifecycle = new PrimitiveLifecycle(primitiveInstance);
                try {
                    lifecycle.init();
                    cache.put(primitive.getKey(), lifecycle);
                } catch (Exception e) {
                    throw new IllegalStateException();
                }
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
        try {
            PrimitiveLifecycle lifecycle = (PrimitiveLifecycle) findInstance(primitiveClass);
            lifecycle.destroy();
            cache.remove(primitiveClass);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public static PrimitiveDiscovery getPrimitiveModuleInstances() {
        if (instance == null) {
            instance = new PrimitiveDiscovery();
            instance.useSPILoader = true;
        }
        return instance;
    }

    public static PrimitiveDiscovery getPrimitiveInstances() {
        return CDI.current().select(PrimitiveDiscovery.class).get();
    }
}
