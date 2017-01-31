package io.descoped.container.module.ext;

import java.util.List;
import java.util.Map;

/**
 * Created by oranheim on 30/01/2017.
 */
public interface InstanceFactory<T> {

    List<Class<?>> discover();

    boolean accept(Class<?> clazz);

    Map<Class<T>, Instance<T>> instances();

    Instance<T> create(Class<T> clazz, T instance);

}
