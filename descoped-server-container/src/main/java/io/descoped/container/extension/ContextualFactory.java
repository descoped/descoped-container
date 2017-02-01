package io.descoped.container.extension;

import io.descoped.container.core.ServerContainer;
import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContextualFactory implements ContextualLifecycle<ServerContainer> {

    private static final Logger log = LoggerFactory.getLogger(ContextualFactory.class);

    private final String id;
    private final static Map<String, ServerContainer> instanceMap = new ConcurrentHashMap<>();

    public ContextualFactory(String id) {
        this.id = id;
    }

    @Override
    public ServerContainer create(Bean<ServerContainer> bean, CreationalContext<ServerContainer> creationalContext) {
        if (!instanceMap.containsKey(id)) {
            ServerContainer instance = ServerExtension.newInstance();
            log.trace("Create core instance: {}", instance);
            instanceMap.put(id, instance);
        }
        ServerContainer instance = (ServerContainer) instanceMap.get(id);
        return instance;
    }

    @Override
    public void destroy(Bean<ServerContainer> bean, ServerContainer instance, CreationalContext<ServerContainer> creationalContext) {
        log.trace("Destroy core instance: {}", instance);
        creationalContext.release();
    }
}
