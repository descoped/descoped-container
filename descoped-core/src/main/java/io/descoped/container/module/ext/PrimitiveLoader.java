package io.descoped.container.module.ext;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitivePriority;

import javax.annotation.Priority;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by oranheim on 31/01/2017.
 */
public class PrimitiveLoader<T> {

    private final InstanceFactory<T> instanceFactory;

    public PrimitiveLoader(final InstanceFactory<T> instanceFactory) {
        this.instanceFactory = instanceFactory;
    }

    private Map<Class<DescopedPrimitive>, Integer> sortMapByPriority(Map<Class<DescopedPrimitive>, Integer> map) {
        return map.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    public void load() {
        List<Class<?>> discoveredClasses = instanceFactory.discover();

        Map<Class<DescopedPrimitive>, Integer> primitivesMap = new LinkedHashMap<>();
        for (Class<?> discoveryClass : discoveredClasses) {
            Integer priority = discoveryClass.isAnnotationPresent(Priority.class) ? discoveryClass.getAnnotation(Priority.class).value() : PrimitivePriority.CORE;
//            instanceFactory.create(discoveredClass, null)
            primitivesMap.put((Class<DescopedPrimitive>) discoveryClass, priority);
        }

        primitivesMap = sortMapByPriority(primitivesMap);

        /*
        for (Map.Entry<Class<DescopedPrimitive>, Integer> primitive : primitivesMap.entrySet()) {
            DescopedPrimitive primitiveInstance = instanceFactory.find(primitive.getKey());
//            DescopedPrimitive lifecycle = new PrimitiveLifecycle(primitiveInstance);
            try {
//                lifecycle.init();
//                cache.put(primitive.getKey(), lifecycle);
            } catch (Exception e) {
                throw new IllegalStateException();
            }
        }
        */

    }

}
