package io.descoped.server;

import io.descoped.server.container.PreStartContainer;
import io.descoped.server.container.UndertowContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 * Created by oranheim on 19/12/2016.
 */
@ApplicationScoped
public class DeploymentHandler {

    private static final Logger log = LoggerFactory.getLogger(DeploymentHandler.class);

    public void observeDeploymentHandlers(@Observes PreStartContainer event) {
        final UndertowContainer container = (UndertowContainer) event.container();
        container.deployJaxRsResourceConfig();
    }

}
