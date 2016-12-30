package io.descoped.container.extension;

import io.descoped.container.support.DescopedServerException;

public final class ClassLoaders {

    private ClassLoaders() {
    }

    public static ClassLoader tccl() {
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            return ClassLoaders.class.getClassLoader();
        }
        return contextClassLoader;
    }

    public static Class<?> loadClass(final String classname) {
        try {
            return tccl().loadClass(classname);
        } catch (final ClassNotFoundException e) {
            throw new DescopedServerException(e);
        }
    }

}
