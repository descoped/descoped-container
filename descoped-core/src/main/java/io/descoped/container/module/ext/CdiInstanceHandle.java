package io.descoped.container.module.ext;

/**
 * Created by oranheim on 30/01/2017.
 */
public class CdiInstanceHandle<T> extends Instance<T> {

    public CdiInstanceHandle(final T instance) {
        super(instance);
    }

    @Override
    public T get() {
        return super.get();
    }

    @Override
    public void release() {
        super.release();
    }
}
