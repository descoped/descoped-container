package io.descoped.container.module.ext;

import io.descoped.container.module.DescopedPrimitive;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by oranheim on 31/01/2017.
 */
public class PrimitiveLoader<T extends DescopedPrimitive> {

    private final InstanceFactory<T> instanceFactory;

    public PrimitiveLoader(final InstanceFactory<T> instanceFactory) {
        this.instanceFactory = instanceFactory;
    }

    private Map<Class<? extends T>, Integer> sortMapByPriority(Map<Class<? extends T>, Integer> map) {
        return map.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

//    public Map<Class<T>, InstanceHandler<T>> load() {
//        List<Class<? extends T>> discoveredClasses = null;
////        List<Class<? extends T>> discoveredClasses = instanceFactory.discover();
//
//        Map<Class<? extends T>, Integer> primitivesMap = new LinkedHashMap<>();
//        for (Class<? extends T> discoveryClass : discoveredClasses) {
//            Integer priority = discoveryClass.isAnnotationPresent(Priority.class) ? discoveryClass.getAnnotation(Priority.class).value() : PrimitivePriority.CORE;
////            instanceFactory.create(discoveredClass, null)
//            primitivesMap.put(discoveryClass, priority);
//        }
//
//        primitivesMap = sortMapByPriority(primitivesMap);
//
//        for (Map.Entry<Class<? extends T>, Integer> primitive : primitivesMap.entrySet()) {
//            InstanceHandler<? extends T> primitiveInstance = instanceFactory.find(primitive.getKey());
//            try {
//                primitiveInstance.get().init();
////                cache.put(primitive.getKey(), lifecycle);
//            } catch (Exception e) {
//                throw new IllegalStateException();
//            }
//        }
//
//        return instanceFactory.instances();
//    }

}
