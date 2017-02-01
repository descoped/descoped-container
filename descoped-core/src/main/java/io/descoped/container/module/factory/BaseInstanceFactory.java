package io.descoped.container.module.factory;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitivePriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by oranheim on 31/01/2017.
 */
abstract public class BaseInstanceFactory<T extends DescopedPrimitive> implements InstanceFactory<T> {

    private final static Logger log = LoggerFactory.getLogger(BaseInstanceFactory.class);
    protected final Class<? extends T> factoryClass;
    private List<Class<T>> discoveredClasses = null;
    private Map<Class<T>, InstanceHandler<T>> instances = new LinkedHashMap<>();

    public BaseInstanceFactory(final Class<? extends T> factoryClass) {
        this.factoryClass = factoryClass;
    }

    abstract protected List<Class<T>> discover();

    abstract protected boolean accept(Class<T> clazz);

    abstract protected InstanceHandler<T> create(Class<T> clazz, Object instance);

    abstract protected Object get(Class<T> primitive);

    private Map<Class<T>, Integer> sortMapByPriority(Map<Class<T>, Integer> map) {
        return map.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    private List<Class<T>> internalDiscovery() {
        List<Class<T>> acceptClasses = new ArrayList<>();
        List<Class<T>> discoveredClasses = discover();
        for (Class<T> discoveredClass : discoveredClasses) {
            if (accept(discoveredClass)) {
                acceptClasses.add(discoveredClass);
            }
        }
        return acceptClasses;
    }

    @Override
    public Map<Class<T>, InstanceHandler<T>> load() {
        discoveredClasses = internalDiscovery();

        // initiate discovered-classes and their priority
        Map<Class<T>, Integer> primitivesMap = new LinkedHashMap<>();
        for (Class<T> discoveryClass : discoveredClasses) {
            Integer priority = discoveryClass.isAnnotationPresent(Priority.class) ? discoveryClass.getAnnotation(Priority.class).value() : PrimitivePriority.CORE;
            primitivesMap.put(discoveryClass, priority);
        }
        primitivesMap = sortMapByPriority(primitivesMap);

        // iterate classes and create internal instance map
        for (Map.Entry<Class<T>, Integer> primitive : primitivesMap.entrySet()) {
            InstanceHandler<T> primitiveInstance = find(primitive.getKey());
//            log.trace("---------> class: {}", primitive.getKey());
//                log.trace("------> class: {} -- inst: {}", primitive.getKey(), primitiveInstance.get());
            try {
                primitiveInstance.get().init();
            } catch (Exception e) {
                throw new IllegalStateException("Error resolving bean: " + primitiveInstance.getType() + " in factory: " + getClass(), e);
            }
        }

        return instances();
    }

    @Override
    public Map<Class<T>, InstanceHandler<T>> instances() {
        return instances;
    }

    @Override
    public InstanceHandler<T> find(Class<T> primitive) {
        if (instances.containsKey(primitive)) {
            return instances.get(primitive);
        } else {
            Object bean = get(primitive);
            if (bean != null) {
                InstanceHandler<T> instanceHandler = create(primitive, bean);
                instances.put(primitive, instanceHandler);
                return instanceHandler;
            }
        }
        return null;
    }

    @Override
    public ListIterator<Map.Entry<Class<T>, InstanceHandler<T>>> reverseOrder() {
        return new ArrayList<>(instances.entrySet()).listIterator(instances.size());
    }

    @Override
    public void remove(Class<T> primitive) {
        InstanceHandler<T> instance = find(primitive);
        try {
            instance.get().destroy();
        } catch (Exception e) {
            throw new IllegalStateException();
        }
        instances.remove(primitive);
        instance.release();
    }

}
