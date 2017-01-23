package io.descoped.container.deployment;

import io.descoped.container.Main;
import io.descoped.container.core.ServerContainer;
import io.descoped.container.core.UndertowContainer;
import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.support.DescopedServerException;
import io.undertow.Handlers;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentManager;
import org.apache.deltaspike.cdise.servlet.CdiServletRequestListener;
import org.apache.deltaspike.servlet.impl.event.EventBridgeContextListener;
import org.apache.deltaspike.servlet.impl.event.EventBridgeFilter;
import org.apache.deltaspike.servlet.impl.produce.RequestResponseHolderFilter;
import org.apache.deltaspike.servlet.impl.produce.RequestResponseHolderListener;
import org.apache.deltaspike.servlet.impl.produce.ServletContextHolderListener;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;

/**
 * Created by oranheim on 20/12/2016.
 */
public class RestDeployment extends BaseDeployment {

    private DeploymentManager manager;
    private PathHandler path;

    public RestDeployment() {
    }

    @Override
    protected DeploymentManager getManager() {
        return manager;
    }

    @Override
    protected PathHandler getPath() {
        return path;
    }

    @Override
    public void deploy(ServerContainer container) {
        try {
            UndertowContainer server = (UndertowContainer) container;
//            DeploymentInfo webapp = newRestDeployment(Main.class.getSimpleName(), server.getContextPath(), "/rest/*", RestResourceConfig.class);
            UndertowWebApp deployment = restDeployment(server);
            manager = Servlets.defaultContainer().addDeployment(deployment.getDeploymentInfo());
            manager.deploy();
            path = Handlers.path(Handlers.redirect(server.getContextPath())).addPrefixPath(container.getContextPath(), manager.start());
            server.builder().setHandler(path);
        } catch (ServletException /*| NoSuchMethodException*/ e) {
            throw new DescopedServerException(e);
        }
    }

    @Override
    public void undeploy(ServerContainer container) {
        try {
            getManager().stop();
            getManager().undeploy();

            manager = null;
            path = null;
        } catch (ServletException e) {
            throw new DescopedServerException(e);
        }
    }

    public static UndertowWebApp restDeployment(UndertowContainer server) {
        UndertowWebApp undertowWebApp = WebApp.create(UndertowWebApp.class)
                .name(Main.class.getSimpleName())
                .contextPath(server.getContextPath())
                .addListener(CdiServletRequestListener.class)
                .addListener(EventBridgeContextListener.class)
                //.addListener(EventBridgeSessionListener.class)
                .addListener(ServletContextHolderListener.class)
                .addListener(RequestResponseHolderListener.class)
                .addServlet("JAX-RS-" + Main.class.getSimpleName(), ServletContainer.class)
                    .addMapping("/rest/*")
                    .setAsyncSupported(true)
                    .setLoadOnStartup(0)
                    .addInitParam(ServletProperties.JAXRS_APPLICATION_CLASS, RestResourceConfig.class.getName())
                    .up()
                .addFilter("RequestResponseHolderFilter", RequestResponseHolderFilter.class)
                    .addFilterUrlMapping("RequestResponseHolderFilter", "/rest/*", DispatcherType.REQUEST)
                    .up()
                .addFilter("EventBridgeFilter", EventBridgeFilter.class)
                    .addFilterUrlMapping("EventBridgeFilter", "/rest/*", DispatcherType.REQUEST)
                    .up()
                .setEagerFilterInit(true)
                ;
        return undertowWebApp;
    }

}
