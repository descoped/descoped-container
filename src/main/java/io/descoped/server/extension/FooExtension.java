package io.descoped.server.extension;

import io.descoped.server.container.UndertowContainer;
import io.descoped.server.support.WebServer;
import io.descoped.server.support.WebServerLiteral;
import org.apache.deltaspike.core.api.literal.AnyLiteral;
import org.apache.deltaspike.core.spi.activation.Deactivatable;
import org.apache.deltaspike.core.util.ClassDeactivationUtils;
import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Typed;
import javax.enterprise.inject.spi.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by oranheim on 22/12/2016.
 */
public class FooExtension<R> implements Deactivatable, Extension {

    private static final Logger log = LoggerFactory.getLogger(FooExtension.class);

    private final Collection<AnnotatedType<?>> webServerTypes = new HashSet<AnnotatedType<?>>();

    private UndertowContainer serverContainer;
    private boolean activated;

    protected void beforeBeanDiscovery(final @Observes BeforeBeanDiscovery bdd) {
        activated = ClassDeactivationUtils.isActivated(FooExtension.class);
        if (!activated) {
            return;
        }
    }

    protected void processAnnotatedType(@Observes ProcessAnnotatedType processAnnotatedType) {
        if (!activated) {
            return;
        }
        final AnnotatedType<R> annotatedType = processAnnotatedType.getAnnotatedType();

        for(AnnotatedField<?> field : annotatedType.getFields()) {
            if (field.isAnnotationPresent(WebServer.class)) {
                if (validateInjectionPoint(field.getJavaMember().getType())) {
                    log.trace("-----> annotatedType={}", annotatedType);
//                    webServerTypes.add(annotatedType);
                }
            }
        }
    }

    private boolean validateInjectionPoint(Class<?> javaClass) {
        boolean ok = true;
        // check class injection type
        log.trace("-----> Validate JavaClass: {}", javaClass);
        return ok;
    }

    protected void afterBeanDiscovery(final @Observes AfterBeanDiscovery abd, final BeanManager bm) {
        if (!activated) {
            return;
        }

//        serverContainer = new UndertowContainer(null);
//        Class<UndertowContainer> clazz = UndertowContainer.class;



        for (AnnotatedType<?> annotatedType : webServerTypes) {
            final Class<?> javaClazz = annotatedType.getJavaClass();
            final BeanBuilder<UndertowContainer> beanBuilder = new BeanBuilder<>(bm);
            beanBuilder
                    .id("DescopedWebServer#" + javaClazz.getName())
                    .passivationCapable(true)
                    .beanClass(annotatedType.getJavaClass())
                    .name(javaClazz.getName())
                    .types(javaClazz, UndertowContainer.class, Object.class, Serializable.class)
                    .scope(ApplicationScoped.class)
                    .qualifiers(new WebServerLiteral("default"), new AnyLiteral())
                    .beanLifecycle(new ContextualFactory());

            log.trace("-----> BeanBuilder: {} => {}", beanBuilder.getName(), javaClazz);
            abd.addBean(beanBuilder.create());
        }
    }

    public void findDynamicProducer(@Observes ProcessBean<DynamicProducerBean> processBean) {
        log.trace("-----> ProcessBean: {}", processBean.getBean());
    }

    @ApplicationScoped
    @Typed(DynamicProducerBean.class)
    static class DynamicProducerBean {

//        @Produces
//        @WebServer
        public UndertowContainer produceUndertowContainer(final InjectionPoint ip) {
            log.trace("-----> IP: {}", ip.getBean());
            return new UndertowContainer(null);
        }

    }

    public void findDynamicConfigurationBeans(@Observes ProcessAnnotatedType<UndertowContainer> pat) {
//        log.trace("=======> pat: {}", pat.getAnnotatedType().getJavaClass());
//        webServerTypes.add(pat.getAnnotatedType());
    }

}
