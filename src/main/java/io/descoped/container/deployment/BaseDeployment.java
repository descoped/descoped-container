package io.descoped.container.deployment;

import io.descoped.container.core.CDIClassIntrospecter;
import io.descoped.container.core.Deployment;
import io.descoped.container.extension.ClassLoaders;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.ServletInfo;
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

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.nio.charset.StandardCharsets;

/**
 * Created by oranheim on 04/01/2017.
 */
abstract public class BaseDeployment implements Deployment {

    abstract protected DeploymentManager getManager();

    abstract protected PathHandler getPath();

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

}
