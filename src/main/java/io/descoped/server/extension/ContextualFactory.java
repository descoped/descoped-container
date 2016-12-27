package io.descoped.server.extension;

import io.descoped.server.container.UndertowContainer;
import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

public class ContextualFactory implements ContextualLifecycle<UndertowContainer> {

    private static final Logger log = LoggerFactory.getLogger(ContextualFactory.class);

    private UndertowContainer server;

    public ContextualFactory() {
        log.trace("-------> Create ContextualFactory");
    }

    @Override
    public UndertowContainer create(Bean<UndertowContainer> bean, CreationalContext<UndertowContainer> creationalContext) {
        if (server == null) {
            server = new UndertowContainer(null);
            log.trace("-------> Create new Container instance");
        }
        log.trace("-------> Use singleton instance");
        return server;
    }

    @Override
    public void destroy(Bean<UndertowContainer> bean, UndertowContainer instance, CreationalContext<UndertowContainer> creationalContext) {
        log.trace("-------> Destroy singleton instance");
        creationalContext.release();
    }
}
