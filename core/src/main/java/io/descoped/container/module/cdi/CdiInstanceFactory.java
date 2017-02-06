package io.descoped.container.module.cdi;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import io.descoped.container.module.factory.BaseInstanceFactory;
import io.descoped.container.module.factory.Instance;
import io.descoped.container.module.factory.InstanceHandler;
import io.descoped.container.util.CdiUtil;
import io.descoped.container.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oranheim on 30/01/2017.
 */
public class CdiInstanceFactory<T extends DescopedPrimitive> extends BaseInstanceFactory<T> {

    public CdiInstanceFactory(Class<? extends T> factoryClass) {
        super(factoryClass);
        System.setProperty("org.jboss.weld.se.shutdownHook", "false");
    }

    @Override
    public String name() {
        return "CDI";
    }

    @Override
    protected List<Class<T>> discover() {
        List<Class<?>> classes = CdiUtil.classesFor(factoryClass);
        return CommonUtil.typedList(classes, factoryClass, ArrayList.class);
    }

    @Override
    public boolean accept(Class<T> clazz) {
        boolean isDescopedModule = clazz.isAnnotationPresent(PrimitiveModule.class);
//        if (isDescopedModule) log.trace("Skip Class: {}", clazz);
        return !isDescopedModule;
    }

    @Override
    public InstanceHandler<T> create(Class<T> clazz, Object instance) {
        InstanceHandler<T> instanceHandler = new Instance<T>((T) instance);
        return instanceHandler;
    }

    @Override
    protected Object get(Class<T> primitive) {
        return CdiUtil.instanceOf(primitive);
    }

}
