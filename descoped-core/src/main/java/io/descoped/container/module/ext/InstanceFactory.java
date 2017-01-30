package io.descoped.container.module.ext;

/**
 * Created by oranheim on 30/01/2017.
 */
public interface InstanceFactory<T> {

    InstanceHandle<T> createInstance();

}
