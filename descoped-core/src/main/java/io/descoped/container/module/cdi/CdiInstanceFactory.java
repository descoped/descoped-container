package io.descoped.container.module.cdi;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import io.descoped.container.module.factory.BaseInstanceFactory;
import io.descoped.container.module.factory.InstanceHandler;
import io.descoped.container.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by oranheim on 30/01/2017.
 */
public class CdiInstanceFactory<T extends DescopedPrimitive> extends BaseInstanceFactory<T> {

    private final static Logger log = LoggerFactory.getLogger(CdiInstanceFactory.class);

    public CdiInstanceFactory(final Class<? extends T> factoryClass) {
        super(factoryClass);
    }

    @Override
    protected List<Class<T>> discover() {
        BeanManager beanManager = CDI.current().getBeanManager();
        Set<Bean<?>> beans = beanManager.getBeans(factoryClass);
        List<Class<?>> classes = beans.stream().map(Bean::getBeanClass).collect(Collectors.toList());
        return CommonUtil.typedList(classes, factoryClass, ArrayList.class);
    }

    @Override
    public boolean accept(Class<T> clazz) {
        boolean isDescopedModule = clazz.isAnnotationPresent(PrimitiveModule.class);
        if (isDescopedModule) log.trace("Skip Class: {}", clazz);
        return !isDescopedModule;
    }

    @Override
    public InstanceHandler<T> create(Class<T> clazz, Object instance) {
        InstanceHandler<T> instanceHandler = new CdiInstanceHandler<T>((Instance<T>) instance);
        return instanceHandler;
    }

    @Override
    protected Object get(Class<T> primitive) {
        return CDI.current().select(primitive);
    }

}
