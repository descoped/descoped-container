package io.descoped.server.extension;

import io.descoped.server.container.ServerContainer;
import io.descoped.server.container.UndertowContainer;
import io.descoped.server.support.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by oranheim on 22/12/2016.
 *
 * This extension implements CDI Lifecylce support for Server containers
 */
public class ServerExtension<R> implements Extension {

    private static final Logger log = LoggerFactory.getLogger(ServerExtension.class);

    private ServerContainer serverContainer;
    private WebServerBean webServerBean;

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
        log.info("beginning the scanning process: {}", bbd);
    }

    void processAnnotatedType(@Observes ProcessAnnotatedType<R> pat, BeanManager beanManager) {
        AnnotatedType<R> at = pat.getAnnotatedType();

        // Check if there is any method defined as Property Resolver
        for (AnnotatedField<? super R> field : at.getFields()) {
            if (field.isAnnotationPresent(WebServer.class)) {
                log.trace("----------------------------------3: {}", field.getJavaMember());
                webServerBean = new WebServerBean(field, beanManager);
                break;
            }
        }
    }

    // http://www.byteslounge.com/tutorials/java-ee-cdi-extension-example

    <X> void processInjectionTarget(@Observes ProcessInjectionTarget<X> pit) {
        log.trace("-------------------------------0: {}", pit.getInjectionTarget());

        final InjectionTarget<X> it = pit.getInjectionTarget();
        final AnnotatedType<X> at = pit.getAnnotatedType();

        if (serverContainer != null) {
            serverContainer = new UndertowContainer(null);
        }


        InjectionTarget<X> wrapper = new InjectionTarget<X>() {

            @Override
            public X produce(CreationalContext<X> ctx) {
                return it.produce(ctx);
            }

            @Override
            public void dispose(X instance) {
                it.dispose(instance);
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return it.getInjectionPoints();
            }

            // The container calls inject() method when it's performing field
            // injection and calling bean initializer methods.
            // Our custom wrapper will also check for fields annotated with
            // @Property and resolve them by invoking the Property Resolver
            // method
            @Override
            public void inject(X instance, CreationalContext<X> ctx) {
                it.inject(instance, ctx);
                for (Field field : at.getJavaClass().getDeclaredFields()) {
                    WebServer annotation = field.getAnnotation(WebServer.class);
                    if (annotation != null) {
//                        String key = annotation.value();
//                        field.setAccessible(true);
//                        try {
//                            field.set(instance, propertyResolverBean
//                                    .resolveProperty(key, ctx));
//                        } catch (IllegalArgumentException
//                                | IllegalAccessException
//                                | InvocationTargetException e) {
//                            throw new RuntimeException(
//                                    "Could not resolve property", e);
//                        }
                    }
                }
            }

            @Override
            public void postConstruct(X instance) {
                it.postConstruct(instance);
            }

            @Override
            public void preDestroy(X instance) {
                it.preDestroy(instance);
            }

        };

        pit.setInjectionTarget(wrapper);
    }


    class WebServerBean {
        private final AnnotatedField<? super R> field;
        private final BeanManager beanManager;

        WebServerBean(AnnotatedField<? super R> field, BeanManager beanManager) {
            this.field = field;
            this.beanManager = beanManager;
        }
    }

}
