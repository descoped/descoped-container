package io.descoped.server.extension;

import io.descoped.server.container.ServerContainer;
import io.descoped.server.container.UndertowContainer;
import io.descoped.server.support.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by oranheim on 22/12/2016.
 * <p>
 * This extension implements CDI Lifecylce support for Server containers
 */
public class ServerExtension<R> implements Extension {

    private static final Logger log = LoggerFactory.getLogger(ServerExtension.class);
    private ServerContainer serverContainer;
    private WebServerBean webServerBean;
    private List<InjectionPoint> webServerInjectionPoints = new ArrayList<>();

    public void processAnnotatedType(@Observes @WithAnnotations({WebServer.class}) ProcessAnnotatedType<R> pat, BeanManager beanManager) {
        AnnotatedType<R> at = pat.getAnnotatedType();
        for (AnnotatedField<? super R> field : at.getFields()) {
            if (field.isAnnotationPresent(WebServer.class)) {
                log.trace("----------------------------------3: {}", field.getJavaMember());
                webServerBean = new WebServerBean(field, beanManager);
                break;
            }
        }
    }

    public void ProcessInjectionPoint(@Observes ProcessInjectionPoint pip) {
//        log.trace("ProcessInjectionPoint: {} => {} => {}", pip.getInjectionPoint().getAnnotated(), pip.getInjectionPoint().getType(), pip.getInjectionPoint().getBean());
    }

    public void ProcessProducerField(@Observes ProcessBean ppf) {
//        log.trace("ProcessProducerField: {}", ppf.getAnnotated());
    }

    public void afterDeploymentValidation(@Observes AfterDeploymentValidation adv) {
        webServerBean.initialize();
    }

    public <X> void processInjectionTarget(@Observes ProcessInjectionTarget<X> pit) {

        final InjectionTarget<X> it = pit.getInjectionTarget();
        final AnnotatedType<X> at = pit.getAnnotatedType();

        boolean isWebServerFieldPresent = false;
        AnnotatedField<? super X> webServerField = null;
        for (AnnotatedField<? super X> field : at.getFields()) {
            if (field.isAnnotationPresent(WebServer.class)) {
                isWebServerFieldPresent = true;
                webServerField = field;
                break;
            }
        }

        if (!isWebServerFieldPresent) {
            return;
        }

        log.trace("......: CREATE INJECTION TARGET {} => {}", webServerField, isWebServerFieldPresent);

        if (serverContainer == null) {
            serverContainer = new UndertowContainer(null);
        }

        final InjectionTarget<X> wrapped = new InjectionTarget<X>() {

            {
                log.trace("instance created");
            }

            @Override
            public void inject(X instance, final CreationalContext<X> ctx) {
                log.info("-------------------> instance={} -- ctx={}", instance, ctx);
                try {
                    it.inject(instance, ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final Field[] fields = at.getJavaClass().getDeclaredFields();
                log.info("-----> INJECTED ^^^: {}", fields.length);
                try {
                    for (Field field : fields) {
                        WebServer annotation = field.getAnnotation(WebServer.class);
                        if (annotation != null) {
////                            log.trace(".... SET WEB CONTAINER: instance={} -- field={}", serverContainer, field.get(instance));
//                            field.set(instance, webServerBean.setValue(ctx));
                            field.setAccessible(true);
                            field.set(instance, webServerBean.setValue(instance, ctx));
//                            field.setAccessible(true);
//                            webServerBean.setValue(ctx, serverContainer);
                        }
                    }
                } catch (/*IllegalArgumentException | IllegalAccessException |*/ Exception e) {
                    e.printStackTrace();
//                    throw new DescopedServerException("Could not resolve injection target", e);
                }

            }

            @Override
            public void postConstruct(X instance) {
                it.postConstruct(instance);
            }


            @Override
            public void preDestroy(X instance) {
                it.dispose(instance);
            }


            @Override
            public void dispose(X instance) {
                it.dispose(instance);
            }


            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return it.getInjectionPoints();
            }


            @Override
            public X produce(CreationalContext<X> ctx) {
                return it.produce(ctx);
            }
        };
        pit.setInjectionTarget(wrapped);

    }

    public class WebServerBean {
        private final AnnotatedField<? super R> webServerField;
        private final BeanManager beanManager;
        private Object webServerInstance;
        private Class<?> injectionClazz;
        private Class<?> injectionFieldClazz;
        private Bean<?> injectionBean;

        WebServerBean(AnnotatedField<? super R> webServerField, BeanManager beanManager) {
            this.webServerField = webServerField;
            this.beanManager = beanManager;
        }

        public void initialize() {
            injectionClazz = webServerField.getJavaMember().getDeclaringClass();
            injectionFieldClazz = webServerField.getJavaMember().getType();
            Set<Bean<?>> beans = beanManager.getBeans(injectionClazz);
            injectionBean = beanManager.resolve(beans);
            log.trace("----------------------------------0.initialize: {} -- {} -- {}", injectionClazz, injectionFieldClazz, injectionBean.getBeanClass());
//            injectionBean.create()

            CreationalContext<?> creationalContext = beanManager.createCreationalContext(injectionBean);
            webServerInstance = beanManager.getReference(injectionBean, injectionClazz, creationalContext);
            log.trace("----x-x--x-x-x-x-x-: {}", webServerInstance);

            webServerInjectionPoints.add(new InjectionPoint() {

                @Override
                public Type getType() {
                    return webServerField.getBaseType();
                }

                @Override
                public Set<Annotation> getQualifiers() {
                    Set<Annotation> qualifiers = new HashSet<Annotation>();
                    for (Annotation annotation : webServerField.getAnnotations()) {
                        if (beanManager.isQualifier(annotation.annotationType())) {
                            qualifiers.add(annotation);
                        }
                    }
                    return qualifiers;
                }

                @Override
                public Bean<?> getBean() {
                    return injectionBean;
                }

                @Override
                public Member getMember() {
                    return webServerField.getJavaMember();
                }

                @Override
                public Annotated getAnnotated() {
                    return webServerField;
                }

                @Override
                public boolean isDelegate() {
                    return false;
                }

                @Override
                public boolean isTransient() {
                    return false;
                }
            });
            log.trace("+++++++++++++++: webServerInjectionPoints={}", webServerInjectionPoints.size());
        }

        public ServerContainer setValue(Object instance, final CreationalContext<?> ctx) throws IllegalAccessException, NoSuchFieldException {

            serverContainer = new UndertowContainer(null);

            log.info("webServerInjectionPoints: {}, injectionClazz={}, injectionInstance={}, injectionFieldName={}, injectionField={}, value=(), fname={}", webServerInjectionPoints.size(), injectionClazz, instance, webServerField.getJavaMember().getName(), injectionFieldClazz, serverContainer, webServerField);


            List<Object> parameters = new ArrayList<>();

//            Field field = instance.getClass().getDeclaredField("server");
//            field.setAccessible(true);
//            field.set(instance, new UndertowContainer(null));
//            injectionFieldClazz.getField("server").set(instance, serverContainer);

            parameters.add(serverContainer);

            for (InjectionPoint parameter : webServerInjectionPoints) {
                parameters.add(beanManager.getInjectableReference(parameter, ctx));
            }

//            webServerField.getJavaMember().setAccessible(true);
//            injectionClazz.getDeclaredField("server").set(instance, webServerField);
            log.info("Set Field={} for Instance={} to Value={} of Type={}", webServerField.getJavaMember().getName(), instance, parameters.get(0), webServerField.getJavaMember().getType());
            webServerField.getJavaMember().setAccessible(true);
            webServerField.getJavaMember().set(instance, parameters.get(0));

            return serverContainer;
        }
    }

}
