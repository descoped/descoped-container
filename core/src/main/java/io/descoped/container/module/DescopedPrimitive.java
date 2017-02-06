package io.descoped.container.module;

import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceHandler;

import javax.enterprise.context.ContextNotActiveException;

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
        InstanceHandler<? extends DescopedPrimitive> instance = DefaultInstanceFactory.findInstance(descopedPrimitive.getClass());
        try {
            return (instance != null ? instance.isRunning() : false);
        } catch (ContextNotActiveException e) {
            e.printStackTrace();
            return false;
        }
    }

}
