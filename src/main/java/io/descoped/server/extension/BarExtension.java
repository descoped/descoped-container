package io.descoped.server.extension;

import io.descoped.server.container.UndertowContainer;
import io.descoped.server.support.WebServerLiteral;
import org.apache.deltaspike.core.util.bean.BeanBuilder;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

/**
 * Created by oranheim on 27/12/2016.
 */
public class BarExtension implements Extension {

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        BeanBuilder<UndertowContainer> beanBuilder = new BeanBuilder<UndertowContainer>(bm)
                .beanClass(UndertowContainer.class)
                .types(UndertowContainer.class, Object.class)
                .qualifiers(new WebServerLiteral())
                // Create a ContextualLifecyle for each id found
                .beanLifecycle(new ContextualFactory());
        // Create and add the Bean
        abd.addBean(beanBuilder.create());
    }

}
