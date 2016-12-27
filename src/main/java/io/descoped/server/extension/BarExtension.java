package io.descoped.server.extension;

import io.descoped.server.container.ServerContainer;
import io.descoped.server.container.UndertowContainer;
import io.descoped.server.support.WebServer;
import io.descoped.server.support.WebServerLiteral;
import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by oranheim on 27/12/2016.
 */
public class BarExtension implements Extension {

    private static final Logger log = LoggerFactory.getLogger(BarExtension.class);

    // @WithAnnotations({WebServer.class})
    private final List<String> ids = new LinkedList<String>();

    public void processInjectionTarget(@Observes ProcessInjectionTarget<?> pit) {
        for (InjectionPoint ip : pit.getInjectionTarget().getInjectionPoints()) {
            WebServer webServer = ip.getAnnotated().getAnnotation(WebServer.class);
            if (webServer != null) {
                // Store the value
                ids.add(webServer.id());
            }
        }
    }

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        for(String id : ids) {
            BeanBuilder<UndertowContainer> beanBuilder = new BeanBuilder<UndertowContainer>(bm)
                    .beanClass(UndertowContainer.class)
                    .types(ServerContainer.class, Object.class)
                    .qualifiers(new WebServerLiteral(id))
                    // Create a ContextualLifecyle for each id found
                    .beanLifecycle(new ContextualFactory());
            // Create and add the Bean
            log.trace("------> Create UndertowContainer bean");
            abd.addBean(beanBuilder.create());
        }
    }

}
