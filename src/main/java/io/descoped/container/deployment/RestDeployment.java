package io.descoped.container.deployment;

import io.descoped.container.core.UndertowContainer;

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
