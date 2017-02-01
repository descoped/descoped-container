package io.descoped.container.module.factory;

import java.util.ListIterator;
import java.util.Map;

/**
 * Created by oranheim on 30/01/2017.
 */
public interface InstanceFactory<T> {

    Map<Class<T>, InstanceHandler<T>> load();

    Map<Class<T>, InstanceHandler<T>> instances();

    ListIterator<Map.Entry<Class<T>, InstanceHandler<T>>> reverseOrder();

    InstanceHandler<T> find(Class<T> clazz);

    void remove(Class<T> primitive);

}
