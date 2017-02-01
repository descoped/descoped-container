package io.descoped.container.extension;

import io.descoped.container.core.ServerContainer;
import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.support.WebServer;
import io.descoped.container.support.WebServerLiteral;
import org.apache.deltaspike.core.api.config.ConfigResolver;
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

    public ServerExtension() {
    }

    public static Class<?> resolveBeanClass() {
        Class<?> clazz = null;
        try {
            String serverContainerClass = ConfigResolver.getPropertyValue("descoped.server.container.class");
            if (serverContainerClass == null) {
                throw new ClassNotFoundException("No config property is set for 'descoped.server.container.class'");
            }
            clazz = Class.forName(serverContainerClass);
        } catch (ClassNotFoundException e) {
            throw new DescopedServerException("Error locating class: " + clazz);
        }
        return clazz;
    }

    public static ServerContainer newInstance() {
        try {
            ServerContainer serverContainer = (ServerContainer) resolveBeanClass().newInstance();
            return serverContainer;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DescopedServerException("Error instantiating class: " + resolveBeanClass());
        }
    }

    public void processInjectionTarget(@Observes ProcessInjectionTarget<?> pit) {
        for (InjectionPoint ip : pit.getInjectionTarget().getInjectionPoints()) {
            WebServer webServer = ip.getAnnotated().getAnnotation(WebServer.class);
            if (webServer != null) {
                log.trace("Processed InjectionPoint: {} => {}", ip, webServer.id());
                injectionPoints.put(ip, webServer.id());
            }
        }
    }

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        Integer n = 0;
        if (injectionPoints.isEmpty()) {
            Bean<?> bean = createBean(WebServerLiteral.DEFAULT.id(), "container", bm);
            abd.addBean(bean);
        } else {
            for (Map.Entry<InjectionPoint, String> entry : injectionPoints.entrySet()) {
                InjectionPoint injectionPoint = entry.getKey();
                String id = entry.getValue();
                String name = injectionPoint.getMember().getName(); //  + "_" + n.toString()

                Bean<?> bean = createBean(id, name, bm);
                log.trace("Register Bean for InjectionPoint: {} => {}", id, name);
                abd.addBean(bean);
                n++;
            }
        }
    }

    private Bean<?> createBean(String id, String name, BeanManager bm) {
        BeanBuilder<ServerContainer> beanBuilder = new BeanBuilder<ServerContainer>(bm)
                .id("DescopedWebServer#" + name)
                .beanClass(resolveBeanClass())
                .types(ServerContainer.class, Object.class)
                .qualifiers(new WebServerLiteral(id))
                .name(name)
                .scope(Dependent.class)
                .beanLifecycle(new ContextualFactory(id));
        Bean<?> bean = beanBuilder.create();
        log.trace("Register Bean for InjectionPoint: {} => {}", id, name);
        return bean;
    }

}
