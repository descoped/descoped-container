package io.descoped.server.extension;

import io.descoped.server.container.ServerContainer;
import io.descoped.server.container.UndertowContainer;
import io.descoped.server.support.WebServer;
import io.descoped.server.support.WebServerLiteral;
import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by oranheim on 22/12/2016.
 * <p>
 * This extension implements CDI Lifecylce support for Server containers
 */
public class ServerExtension implements Extension {

    private static final Logger log = LoggerFactory.getLogger(ServerExtension.class);

    private final Map<InjectionPoint, String> injectionPoints = new ConcurrentHashMap();

    public void processInjectionTarget(@Observes ProcessInjectionTarget<?> pit) {
        for (InjectionPoint ip : pit.getInjectionTarget().getInjectionPoints()) {
            WebServer webServer = ip.getAnnotated().getAnnotation(WebServer.class);
            if (webServer != null) {
                log.trace("--> IP: {}", ip);
                injectionPoints.put(ip, webServer.id());
            }
        }
    }

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        log.trace("--------> Size: {}", injectionPoints.size());
        for (Map.Entry<InjectionPoint, String> entry : injectionPoints.entrySet()) {
            InjectionPoint injectionPoint = entry.getKey();
            String id = entry.getValue();
            String name = injectionPoint.getMember().getName();

            BeanBuilder<UndertowContainer> beanBuilder = new BeanBuilder<UndertowContainer>(bm)
                    .id("DescopedWebServer#" + name)
                    .beanClass(UndertowContainer.class)
                    .types(ServerContainer.class, Object.class)
                    .qualifiers(new WebServerLiteral(id))
                    .name(name)
                    .scope(Dependent.class)
                    .beanLifecycle(new ContextualFactory(id));
            Bean<?> bean = beanBuilder.create();
            log.trace("--< IP: {} ==> {} <= {}", entry, name, bean.getName());
            abd.addBean(bean);
        }
    }

}
