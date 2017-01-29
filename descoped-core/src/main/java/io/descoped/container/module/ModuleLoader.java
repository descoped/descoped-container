package io.descoped.container.module;

import io.descoped.container.exception.DescopedServerException;

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
            if (module.getClass().isAnnotationPresent(PrimitiveModule.class)) {
                classes.add(module.getClass());
            } else {
                throw new DescopedServerException("You must annotate '" + module.getClass().getName() + "' with @PrimitiveModule when using SPI! Current declaration is in conflict with CDI primitives.");
            }
        });
        return classes;
    }

    public static <T extends DescopedPrimitive> T get(Class<T> clazz) {
        ServiceLoader<?> modules = serviceLoader();
        for(Iterator<?> it = modules.iterator(); it.hasNext(); ) {
            Object module = it.next();
            if (clazz == module.getClass() && module.getClass().isAnnotationPresent(PrimitiveModule.class)) {
                return (T) module;
            } else {
                throw new DescopedServerException("You must annotate '" + module.getClass().getName() + "' with @PrimitiveModule when using SPI! Current declaration is in conflict with CDI primitives.");
            }
        }
        return null;
    }
}
