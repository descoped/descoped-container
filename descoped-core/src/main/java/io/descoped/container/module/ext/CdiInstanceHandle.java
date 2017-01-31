package io.descoped.container.module.ext;

/**
 * Created by oranheim on 30/01/2017.
 */
public class CdiInstanceHandle<T> implements InstanceHandle<T> {

    private final Instance<T> instance;

    public CdiInstanceHandle(final Instance<T> instance) {
        this.instance = instance;
    }

    @Override
    public T get() {
        return instance.get();
    }

    @Override
    public void release() {
        instance.release();
    }

}
