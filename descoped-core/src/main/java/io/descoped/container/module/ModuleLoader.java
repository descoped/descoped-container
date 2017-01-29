package io.descoped.container.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by oranheim on 29/01/2017.
 */
public class ModuleLoader {

    private static ServiceLoader<DescopedPrimitive> serviceLoader() {
        ServiceLoader<DescopedPrimitive> modules = ServiceLoader.load(DescopedPrimitive.class);
        return modules;
    }
    
    public static List<Class<?>> discover() {
        List<Class<?>> classes = new ArrayList<>();
        serviceLoader().forEach(module -> {
            classes.add(module.getClass());
        });
        return classes;
    }

    public static <T extends DescopedPrimitive> T get(Class<T> clazz) {
        ServiceLoader<?> modules = serviceLoader();
        for(Iterator<?> it = modules.iterator(); it.hasNext(); ) {
            Object module = it.next();
            if (clazz == module.getClass()) {
                return (T) module;
            }
        }
        return null;
    }
}
