package io.descoped.container.module.ext;

import io.descoped.container.module.DescopedPrimitive;

/**
 * Created by oranheim on 30/01/2017.
 */
public class CdiClassFactory implements ClassFactory {

    public CdiClassFactory() {
        CdiInstanceFactory<DescopedPrimitive> factory = new CdiInstanceFactory<>(DescopedPrimitive.class);
//        InstanceHandle<DescopedPrimitive> instance = factory.createInstance();
//        DescopedPrimitive obj = instance.get();
    }
    
    @Override
    public <T> InstanceFactory<T> createInstanceFactory(Class<T> clazz) {
        return null;
    }
}
