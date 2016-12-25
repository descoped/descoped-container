package io.descoped.server.extension;

import io.descoped.server.container.UndertowContainer;
import io.descoped.server.support.WebServer;
import org.apache.deltaspike.core.api.literal.AnyLiteral;
import org.apache.deltaspike.core.api.literal.DefaultLiteral;
import org.apache.deltaspike.core.spi.activation.Deactivatable;
import org.apache.deltaspike.core.util.ClassDeactivationUtils;
import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
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
                    webServerTypes.add(annotatedType);
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

        serverContainer = new UndertowContainer(null);
        Class<UndertowContainer> clazz = UndertowContainer.class;

        for (AnnotatedType<?> annotatedType : webServerTypes) {
            final BeanBuilder<UndertowContainer> beanBuilder = new BeanBuilder<>(bm);
            beanBuilder
                    .passivationCapable(false)
                    .beanClass(clazz)
                    .name(clazz.getName())
                    .types(annotatedType.getJavaClass(), UndertowContainer.class, Object.class)
                    .scope(ApplicationScoped.class)
                    .qualifiers(new DefaultLiteral(), new AnyLiteral())
//                    .addQualifier(new DefaultLiteral())
                    .beanLifecycle(new ContextualFactory<UndertowContainer>());
            log.trace("-----> BeanBuilder: {}", beanBuilder.getName());
//            beanBuilder.qualifiers(WebServer.class);

            abd.addBean(beanBuilder.create());
        }

    }


}
