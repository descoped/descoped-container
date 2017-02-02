package io.descoped.container.service;

import io.descoped.container.core.ServerContainer;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.Primitive;
import io.descoped.container.module.PrimitivePriority;
import io.descoped.container.support.WebServerLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Singleton;

/**
 * Created by oranheim on 28/01/2017.
 */
@Priority(PrimitivePriority.FRAMEWORK)
@Primitive
@Singleton
public class WebServerPrimitive implements DescopedPrimitive {

    private static final Logger log = LoggerFactory.getLogger(WebServerPrimitive.class);

    private ServerContainer serverContainer;

    public int getPort() {
        return (serverContainer == null ? 8080 : serverContainer.getPort());
    }

    @Override
    public void init() {
    }

    @Override
    public void start() {
        if (serverContainer == null) {
            serverContainer = CDI.current().select(ServerContainer.class, WebServerLiteral.DEFAULT).get();
            serverContainer.start();
        }
    }

    @Override
    public void stop() {
        if (serverContainer != null) {
            log.trace("START SHUTDOWN");
            serverContainer.shutdown();
            log.trace("DONE SHUTDOWN");
            serverContainer = null;
        }
    }

    @Override
    public void destroy() {
    }

}
