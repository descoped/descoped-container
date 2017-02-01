package io.descoped.container.module.factory;

/**
 * Created by oranheim on 30/01/2017.
 */
public interface InstanceHandler<T> {

    Class<T> getType();

    T get();

    boolean isRunning();

    void release();

}
