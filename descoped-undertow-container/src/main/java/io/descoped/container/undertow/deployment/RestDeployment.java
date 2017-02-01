package io.descoped.container.undertow.deployment;

import io.descoped.container.undertow.core.UndertowContainer;

/**
 * Created by oranheim on 20/12/2016.
 */
public class RestDeployment extends UndertowDeployment {

    public RestDeployment() {
    }

    @Override
    public UndertowWebApp deployment(UndertowContainer container) {
        return restDeployment(container, "/rest/*");
    }

}
