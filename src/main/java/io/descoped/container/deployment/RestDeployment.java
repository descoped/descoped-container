package io.descoped.container.deployment;

import io.descoped.container.Main;
import io.descoped.container.core.CDIClassIntrospecter;
import io.descoped.container.core.ServerContainer;
import io.descoped.container.core.UndertowContainer;
import io.descoped.container.extension.ClassLoaders;
import io.descoped.container.support.DescopedServerException;
import io.undertow.Handlers;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import org.apache.deltaspike.cdise.servlet.CdiServletRequestListener;
import org.apache.deltaspike.servlet.impl.event.EventBridgeContextListener;
import org.apache.deltaspike.servlet.impl.event.EventBridgeFilter;
import org.apache.deltaspike.servlet.impl.produce.RequestResponseHolderFilter;
import org.apache.deltaspike.servlet.impl.produce.RequestResponseHolderListener;
import org.apache.deltaspike.servlet.impl.produce.ServletContextHolderListener;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import java.nio.charset.StandardCharsets;

/**
 * Created by oranheim on 20/12/2016.
 */
public class RestDeployment implements io.descoped.container.core.Deployment {

    private DeploymentManager manager;
    private PathHandler path;

    public RestDeployment() {
    }

    public DeploymentManager getManager() {
        return manager;
    }

    public PathHandler getPath() {
        return path;
    }

    private ClassIntrospecter getClassIntrospecter() {
        return CDI.current().select(CDIClassIntrospecter.class).get();
    }

    private DeploymentInfo getDeployment(String contextPath) {
        ListenerInfo listenerInfo = Servlets.listener(CdiServletRequestListener.class);

        DeploymentInfo webapp = Servlets.deployment()
//                .setClassIntrospecter(getClassIntrospecter())
                .addListener(listenerInfo)
                .addListener(Servlets.listener(EventBridgeContextListener.class))
//                .addListener(Servlets.listener(EventBridgeSessionListener.class))
                .addListener(Servlets.listener(ServletContextHolderListener.class))
                .addListener(Servlets.listener(RequestResponseHolderListener.class))
                .addFilter(new FilterInfo("RequestResponseHolderFilter", RequestResponseHolderFilter.class))
                .addFilterUrlMapping("RequestResponseHolderFilter", "/*", DispatcherType.REQUEST)
                .addFilter(new FilterInfo("EventBridgeFilter", EventBridgeFilter.class))
                .addFilterUrlMapping("EventBridgeFilter", "/*", DispatcherType.REQUEST)
                .setClassLoader(ClassLoaders.tccl())
                .setContextPath(contextPath)
                .setDefaultEncoding(StandardCharsets.UTF_8.displayName())
                .setDeploymentName(Main.class.getSimpleName())
                .setDisplayName(Main.class.getSimpleName())
                .setEagerFilterInit(true);

        ServletInfo holder = Servlets.servlet(RestResourceConfig.class.getName(), ServletContainer.class)
                .setLoadOnStartup(0)
                .setAsyncSupported(true)
                .setEnabled(true)
                .addMapping("/*")
                .addInitParam(ServletProperties.JAXRS_APPLICATION_CLASS, RestResourceConfig.class.getName());
        webapp.addServlet(holder);

        return webapp;
    }

    @Override
    public void deploy(ServerContainer container) {
        try {
            UndertowContainer server = (UndertowContainer) container;
            DeploymentInfo webapp = getDeployment(server.getContextPath());
            manager = Servlets.newContainer().addDeployment(webapp);
            manager.deploy();
            path = Handlers.path(Handlers.redirect(server.getContextPath())).addPrefixPath(container.getContextPath(), manager.start());
            server.builder().setHandler(path);
        } catch (ServletException e) {
            throw new DescopedServerException(e);
        }
    }

    @Override
    public void undeploy(ServerContainer container) {
        try {
            manager.stop();
            manager.undeploy();

//            Servlets.deployment().getListeners().clear();
//            Servlets.deployment().getFilters().clear();
//            ServletContextHolderHelper.release();

            manager = null;
            path = null;
        } catch (ServletException e) {
            throw new DescopedServerException(e);
        }
    }

}
