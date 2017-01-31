package io.descoped.container.module.ext;

/**
 * Created by oranheim on 30/01/2017.
 */
public class Instance<T> implements InstanceHandle<T> {

    protected T instance;

    public Instance(final T instance) {
        this.instance = instance;
    }

    @Override
    public T get() {
        return instance;
    }

    @Override
    public void release() {
        this.instance = null;
    }

}
