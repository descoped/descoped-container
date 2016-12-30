package io.descoped.container.extension;

import io.descoped.container.core.ServerContainer;
import io.descoped.container.core.UndertowContainer;
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
        if (!instanceMap.containsKey(id)) {
            UndertowContainer instance = new UndertowContainer();
            log.trace("Create core instance: {}", instance);
            instanceMap.put(id, instance);
        }
        UndertowContainer instance = (UndertowContainer) instanceMap.get(id);
        return instance;
    }

    @Override
    public void destroy(Bean<UndertowContainer> bean, UndertowContainer instance, CreationalContext<UndertowContainer> creationalContext) {
        log.trace("Destroy core instance: {}", instance);
        creationalContext.release();
    }
}
