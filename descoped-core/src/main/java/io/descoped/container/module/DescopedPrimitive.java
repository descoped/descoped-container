package io.descoped.container.module;

import io.descoped.container.module.factory.DefaultInstanceFactory;

/**
 * Created by oranheim on 27/01/2017.
 */
public interface DescopedPrimitive {

    void init();

    void start();

    void stop();

    void destroy();

    default boolean isRunning() {
        return isRunning(this);
    }

    static boolean isRunning(DescopedPrimitive descopedPrimitive) {
        return DefaultInstanceFactory.findInstance(descopedPrimitive.getClass()).isRunning();
    }

}
