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
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletException;
import javax.ws.rs.core.Application;
import java.nio.charset.StandardCharsets;

/**
 * Created by oranheim on 20/12/2016.
 */
public class RestDeployment implements io.descoped.container.core.Deployment {

    private DeploymentManager manager;
    private PathHandler path;

    public RestDeployment() {
    }

    private ClassIntrospecter getClassIntrospecter() {
        return CDI.current().select(CDIClassIntrospecter.class).get();
    }

    private DeploymentInfo getDeployment(String contextPath) {
        ListenerInfo listenerInfo = Servlets.listener(CdiServletRequestListener.class);

        DeploymentInfo webapp = Servlets.deployment()
//                .setClassIntrospecter(getClassIntrospecter())
                .addListener(listenerInfo)
                .setClassLoader(ClassLoaders.tccl())
                .setContextPath(contextPath)
                .setDefaultEncoding(StandardCharsets.UTF_8.displayName())
                .setDeploymentName(Main.class.getSimpleName())
                .setDisplayName(Main.class.getSimpleName())
                .setEagerFilterInit(true);
        ;

        Class<? extends Application> app = RestResourceConfig.class;
        if (app != null) {
            ServletInfo holder = Servlets.servlet(app.getName(), ServletContainer.class)
                    .setLoadOnStartup(0)
                    .setAsyncSupported(true)
                    .setEnabled(true)
                    .addMapping("/*")
                    .addInitParam(ServletProperties.JAXRS_APPLICATION_CLASS, app.getName());
            webapp.addServlet(holder);
        }

        return webapp;
    }

    @Override
    public void deploy(ServerContainer container) {
        try {
            UndertowContainer server = (UndertowContainer) container;
            DeploymentInfo webapp = getDeployment(server.getContextPath());
            manager = Servlets.defaultContainer().addDeployment(webapp);
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

            manager = null;
            path = null;
        } catch (ServletException e) {
            throw new DescopedServerException(e);
        }
    }

}
