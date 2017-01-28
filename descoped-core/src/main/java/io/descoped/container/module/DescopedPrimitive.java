package io.descoped.container.module;

import io.descoped.container.Main;

/**
 * Created by oranheim on 27/01/2017.
 */
public interface DescopedPrimitive {

    void init();

    void start();

    void stop();

    void destroy();

    static boolean isRunning(DescopedPrimitive descopedPrimitive) {
        Class<? extends DescopedPrimitive> clazz = (descopedPrimitive instanceof PrimitiveLifecycle ? ((PrimitiveLifecycle) descopedPrimitive).internalClass() : descopedPrimitive.getClass());
        PrimitiveLifecycle primitive = (PrimitiveLifecycle) Main.findServiceInstance(clazz);
        if (primitive != null) {
            return primitive.isRunning();
        }
//        return false;
        throw new IllegalStateException();
    }

}
