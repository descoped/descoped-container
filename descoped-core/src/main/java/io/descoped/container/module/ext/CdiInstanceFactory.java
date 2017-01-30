package io.descoped.container.module.ext;

import javax.enterprise.inject.Vetoed;

/**
 * Created by oranheim on 30/01/2017.
 */
@Vetoed
public class CdiInstanceFactory<T> implements InstanceFactory<T> {

    @Override
    public InstanceHandle<T> createInstance() {
        return new CdiInstanceHandle<T>(null);
    }

    // ---

//    private static

}
