package io.descoped.container.deployment;

import io.descoped.container.deployment.api.WebApp;

/**
 * Created by oranheim on 23/01/2017.
 */
public class WebAppImpl implements WebApp<WebAppImpl> {

    @Override
    public WebAppImpl name(String name) {
        return null;
    }

    @Override
    public WebAppImpl contextPath(String contextPath) {
        return null;
    }

    @Override
    public WebAppImpl addListener(Class<?> listenerClass) {
        return null;
    }
}
