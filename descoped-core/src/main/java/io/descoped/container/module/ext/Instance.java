package io.descoped.container.module.ext;

/**
 * Created by oranheim on 30/01/2017.
 */
public class Instance<T> {

    private T instance;

    public Instance(final T instance) {
        this.instance = instance;
    }

    public T get() {
        return instance;
    }

    public void release() {
        this.instance = null;
    }
}
