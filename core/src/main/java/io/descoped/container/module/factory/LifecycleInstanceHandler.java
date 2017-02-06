package io.descoped.container.module.factory;

import org.jboss.weld.bean.proxy.ProxyObject;

/**
 * Created by oranheim on 01/02/2017.
 */
abstract public class LifecycleInstanceHandler<T> implements InstanceHandler<T> {

    private boolean running = false;

    private boolean isProxyClass(T primitive) {
        return ProxyObject.class.isAssignableFrom(primitive.getClass());
    }

    private Class<T> normalizeProxyClass(T primitive) {
        Class<T> primitiveClass = (isProxyClass(primitive) ? (Class<T>) primitive.getClass().getSuperclass() : (Class<T>) primitive.getClass());
        return primitiveClass;
    }

    @Override
    public Class<T> getType() {
        return (isProxyClass(get()) ? normalizeProxyClass(get()) : (Class<T>) get().getClass());
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
