package io.descoped.server.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

/**
 * Created by oranheim on 19/12/2016.
 */
@ApplicationScoped
public class DeploymentHandler {

    private static final Logger log = LoggerFactory.getLogger(DeploymentHandler.class);

    public void observeDeploymentHandlers(@Observes ApplicationStartupEvent event) {
        log.info("==============================> event: {}", event);
    }


}
