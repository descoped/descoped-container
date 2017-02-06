package io.descoped.container.undertow.deployment;

import io.descoped.container.Main;
import io.descoped.container.core.Deployment;
import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.extension.ClassLoaders;
import io.descoped.container.undertow.core.CDIClassIntrospecter;
import io.descoped.container.undertow.core.UndertowContainer;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import org.apache.deltaspike.cdise.servlet.CdiServletRequestListener;
import org.apache.deltaspike.servlet.impl.event.EventBridgeContextListener;
import org.apache.deltaspike.servlet.impl.event.EventBridgeFilter;
import org.apache.deltaspike.servlet.impl.produce.RequestResponseHolderFilter;
import org.apache.deltaspike.servlet.impl.produce.RequestResponseHolderListener;
import org.apache.deltaspike.servlet.impl.produce.ServletContextHolderListener;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.jboss.weld.servlet.WeldInitialListener;
import org.jboss.weld.servlet.WeldTerminalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.HandlesTypes;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by oranheim on 04/01/2017.
 */
abstract public class BaseDeployment implements Deployment {

    private static final Logger log = LoggerFactory.getLogger(BaseDeployment.class);

    abstract protected DeploymentManager getManager();

    abstract protected PathHandler getPath();

    protected List<ServletContainerInitializerInfo> getServletContainerInitializers() {
        List<ServletContainerInitializerInfo> infos = new ArrayList<>();
        for (ServletContainerInitializer initializer : ServiceLoader.load(ServletContainerInitializer.class)) {
            HandlesTypes types = initializer.getClass().getAnnotation(HandlesTypes.class);
            log.trace("HandleTypes: {} => {}", initializer.getClass(), types);
            infos.add(new ServletContainerInitializerInfo(
                    initializer.getClass(),
                    new ImmediateInstanceFactory<>(initializer),
                    types == null ? Collections.emptySet() : new LinkedHashSet<>(Arrays.asList(types.value()))));
        }
        return infos;
    }

    protected ServletInfo createServletInfo(String servletName, String mapping, Class<? extends Servlet> servlet,
                                            Class<? extends ResourceConfig> jaxRsResourceClass,
                                            boolean asyncSupported, int loadOnStartup) throws NoSuchMethodException {
        ServletInfo servletInfo = Servlets
                .servlet(servletName, servlet)
                .setAsyncSupported(asyncSupported)
                .setLoadOnStartup(loadOnStartup)
                .addMapping(mapping)
                .addInitParam(ServletProperties.JAXRS_APPLICATION_CLASS, jaxRsResourceClass.getName());
        servletInfo.setInstanceFactory(CDIClassIntrospecter.INSTANCE.createInstanceFactory(servlet));
        return servletInfo;
    }

    protected FilterInfo createFilter(String filterName, Class<? extends Filter> filterClass) {
        return new FilterInfo(filterName, filterClass);
    }

    protected DeploymentInfo newMvcDeployment(String deploymentName, String contextPath, String mapping, String resourcePath, Class<? extends ResourceConfig> jaxRsResourceClass) throws ServletException, NoSuchMethodException {
        ClassLoader classLoader = ClassLoaders.tccl();
        DeploymentInfo deploymentInfo = Servlets.deployment()
                .addListener(Servlets.listener(WeldInitialListener.class))
                .addListener(Servlets.listener(WeldTerminalListener.class))
                .setContextPath(contextPath)
                .setDeploymentName(deploymentName)
                .addServlet(
                        createServletInfo("JAX-RS-" + deploymentName, mapping, ServletContainer.class, jaxRsResourceClass, true, 1)
                )
                .setResourceManager(new ClassPathResourceManager(classLoader, resourcePath))
                .setClassIntrospecter(CDIClassIntrospecter.INSTANCE)
                //.setAllowNonStandardWrappers(true)
                .setDefaultEncoding(StandardCharsets.UTF_8.displayName())
                .setClassLoader(classLoader);
        return deploymentInfo;
    }

    // either use Weld or DS scope listeners
    protected DeploymentInfo newRestDeployment(String deploymentName, String contextPath, String mapping, Class<? extends ResourceConfig> jaxRsResourceClass) throws NoSuchMethodException {
        ClassLoader classLoader = ClassLoaders.tccl();
        DeploymentInfo deploymentInfo = Servlets.deployment()
//                .addListener(Servlets.listener(WeldInitialListener.class))
//                .addListener(Servlets.listener(WeldTerminalListener.class))
                .addListener(Servlets.listener(CdiServletRequestListener.class))
                .addListener(Servlets.listener(EventBridgeContextListener.class))
                //.addListener(Servlets.listener(EventBridgeSessionListener.class))
                .addListener(Servlets.listener(ServletContextHolderListener.class))
                .addListener(Servlets.listener(RequestResponseHolderListener.class))
                .setContextPath(contextPath)
                .setDeploymentName(deploymentName)
                .addServlet(
                        createServletInfo("JAX-RS-" + deploymentName, mapping, ServletContainer.class, jaxRsResourceClass, true, 0)
                )
                .addFilter(createFilter("RequestResponseHolderFilter", RequestResponseHolderFilter.class))
                .addFilterUrlMapping("RequestResponseHolderFilter", mapping, DispatcherType.REQUEST)
                .addFilter(createFilter("EventBridgeFilter", EventBridgeFilter.class))
                .addFilterUrlMapping("EventBridgeFilter", mapping, DispatcherType.REQUEST)
                .setEagerFilterInit(true)
                .setClassIntrospecter(CDIClassIntrospecter.INSTANCE)
                .setDefaultEncoding(StandardCharsets.UTF_8.displayName())
                .setClassLoader(classLoader);
        return deploymentInfo;
    }

    public static UndertowWebApp restDeployment(UndertowContainer server, String mapping) {
        UndertowWebApp undertowWebApp = WebApp.create(UndertowWebApp.class)
                .name(Main.class.getSimpleName())
                .contextPath(server.getContextPath())
                .addListener(CdiServletRequestListener.class)
                .addListener(EventBridgeContextListener.class)
                //.addListener(EventBridgeSessionListener.class)
                .addListener(ServletContextHolderListener.class)
                .addListener(RequestResponseHolderListener.class)
                .addServlet("JAX-RS-" + Main.class.getSimpleName(), ServletContainer.class)
                    .addMapping(mapping)
                    .setAsyncSupported(true)
                    .setLoadOnStartup(0)
                    .addInitParam(ServletProperties.JAXRS_APPLICATION_CLASS, RestResourceConfig.class.getName())
                    .up()
                .addFilter("RequestResponseHolderFilter", RequestResponseHolderFilter.class)
                    .addFilterUrlMapping(mapping, DispatcherType.REQUEST)
                    .up()
                .addFilter("EventBridgeFilter", EventBridgeFilter.class)
                    .addFilterUrlMapping(mapping, DispatcherType.REQUEST)
                    .up()
                .setEagerFilterInit(true)
                ;
        return undertowWebApp;
    }

    public static UndertowWebApp mvcDeployment(UndertowContainer server, Class<? extends ResourceConfig> resourceConfig, String mapping, String resourcePath) {
        UndertowWebApp undertowWebApp = WebApp.create(UndertowWebApp.class)
                .name(Main.class.getSimpleName())
                .contextPath(server.getContextPath())
                .addListener(WeldInitialListener.class)
                .addListener(WeldTerminalListener.class)
                .addServlet("JAX-RS-MVC-" + Main.class.getSimpleName(), ServletContainer.class)
                    .addMapping(mapping)
                    .setAsyncSupported(true)
                    .setLoadOnStartup(1)
                    .addInitParam(ServletProperties.JAXRS_APPLICATION_CLASS, resourceConfig.getName())
                    .up()
                .addFilter("RequestResponseHolderFilter", RequestResponseHolderFilter.class)
                    .addFilterUrlMapping(mapping, DispatcherType.REQUEST)
                    .up()
                .addFilter("EventBridgeFilter", EventBridgeFilter.class)
                    .addFilterUrlMapping(mapping, DispatcherType.REQUEST)
                    .up()
                .addWelcomePage("index.html")
                ;
        undertowWebApp.getDeploymentInfo().setResourceManager(new ClassPathResourceManager(ClassLoaders.tccl(), resourcePath));
        return undertowWebApp;
    }
}
