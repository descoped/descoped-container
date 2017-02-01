package io.descoped.container.module.ext.cdi;

import io.descoped.container.module.ext.LifecycleInstanceHandler;

import javax.enterprise.inject.Instance;

/**
 * Created by oranheim on 31/01/2017.
 */
public class CdiInstanceHandler<T> extends LifecycleInstanceHandler<T> {

    private final Instance<T> instance;
    private T found;

    public CdiInstanceHandler(final Instance<T> instance) {
        this.instance = instance;
    }

    @Override
    public T get() {
        if (found == null) {
            found = instance.get();
        }
        return found;
    }

    @Override
    public void release() {
        instance.destroy(found);
    }
}
