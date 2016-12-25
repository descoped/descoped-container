package io.descoped.server.extension;

import io.descoped.server.container.ServerContainer;
import io.descoped.server.container.UndertowContainer;
import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

public class ContextualFactory<T> implements ContextualLifecycle<T> {

    private ServerContainer server;
    private Object webServerInstance;

    public ContextualFactory() {

    }

    @Override
    public T create(final Bean<T> bean, final CreationalContext<T> creationalContext) {
        final BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
        webServerInstance = beanManager.getReference(bean, UndertowContainer.class, creationalContext);
        return (T) webServerInstance;
    }

    @Override
    public void destroy(final Bean<T> bean, final T instance, final CreationalContext<T> creationalContext) {
        bean.destroy(instance, creationalContext);
        webServerInstance = null;
        server = null;
    }
}
