package io.descoped.server.extension;

import io.descoped.server.container.ServerContainer;
import io.descoped.server.container.UndertowContainer;
import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContextualFactory implements ContextualLifecycle<UndertowContainer> {

    private static final Logger log = LoggerFactory.getLogger(ContextualFactory.class);

    private final String id;
    private final static Map<String, ServerContainer> instanceMap = new ConcurrentHashMap<>();

    public ContextualFactory(String id) {
        this.id = id;
    }

    @Override
    public UndertowContainer create(Bean<UndertowContainer> bean, CreationalContext<UndertowContainer> creationalContext) {
        log.trace("......................................: {}", id);
        if (!instanceMap.containsKey(id)) {
            log.trace("----> Create instance for id: {}", id);
            UndertowContainer container = new UndertowContainer(null);
            instanceMap.put(id, container);
        }
        UndertowContainer instance = (UndertowContainer) instanceMap.get(id);
        log.trace("-----<< Get Instance: {}", instance);
        return instance;
    }

    @Override
    public void destroy(Bean<UndertowContainer> bean, UndertowContainer instance, CreationalContext<UndertowContainer> creationalContext) {
        log.trace("-------> Destroy singleton instance: {}", instance);
        creationalContext.release();
    }
}
