package io.descoped.container.module.factory;

/**
 * Created by oranheim on 01/02/2017.
 */
abstract public class LifecycleInstanceHandler<T> implements InstanceHandler<T> {

    private boolean running = false;

    private Class<?> resolveWeldProxyObjectClass() {
        try {
            Class<?> clazz = Class.forName("org.jboss.weld.bean.proxy.ProxyObject");
            return clazz;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private boolean isAssignableFrom(Class<?> isAssignableFromClass, T instance) {
        return (isAssignableFromClass != null && instance != null ? isAssignableFromClass.isAssignableFrom(instance.getClass()) : false);
    }

    private boolean isProxyClass(T primitive) {
        Class<?> proxyObjectClass = resolveWeldProxyObjectClass();
        return (proxyObjectClass != null ? isAssignableFrom(proxyObjectClass, primitive) : false);
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
