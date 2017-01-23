package io.descoped.container.deployment.api;

import java.util.ServiceLoader;

/**
 * Created by oranheim on 23/01/2017.
 */
public interface WebApp<T extends WebApp> {

    T name(String name);

    T contextPath(String contextPath);

    T addListener(Class<?> listenerClass);


    static <T> T create() {
        ServiceLoader<WebApp> serviceLoader = ServiceLoader.load(WebApp.class);
        WebApp o = serviceLoader.iterator().next();
        return null;
    }

}
