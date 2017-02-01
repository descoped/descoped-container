package io.descoped.container.module.ext;

/**
 * Created by oranheim on 30/01/2017.
 */
public interface InstanceHandler<T> {

    T get();

    boolean isRunning();

    void release();

}
