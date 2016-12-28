package io.descoped.server.deployment;

import io.descoped.server.Main;
import io.descoped.server.container.CDIClassIntrospecter;
import io.descoped.server.container.ServerContainer;
import io.descoped.server.container.UndertowContainer;
import io.descoped.server.extension.ClassLoaders;
import io.descoped.server.support.DescopedServerException;
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

import static io.descoped.server.container.ServerContainer.getContextPath;

/**
 * Created by oranheim on 20/12/2016.
 */
public class RestDeployment implements io.descoped.server.container.Deployment {

    private PathHandler path;
    private ListenerInfo listenerInfo;
    private DeploymentInfo webapp;
    private DeploymentManager manager;

    public RestDeployment() {
    }

    public static RestDeployment newInstance() {
        return new RestDeployment();
    }

    private ClassIntrospecter getClassIntrospecter() {
        return CDI.current().select(CDIClassIntrospecter.class).get();
    }

    private DeploymentInfo getDeployment() {
        listenerInfo = Servlets.listener(CdiServletRequestListener.class);

        webapp = Servlets.deployment()
//                .setClassIntrospecter(getClassIntrospecter())
                .addListener(listenerInfo)
                .setClassLoader(ClassLoaders.tccl())
                .setContextPath(getContextPath())
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

    // this is necessary because of the registration of Servlets.listener(CdiServletRequestListener.class)
    // todo: need to find a way to unregister CdiServletRequestListener in Jersey
    private boolean isNotDeployed() {
        return path == null;
//        return false;
    }

    @Override
    public void deploy(ServerContainer container) {
        try {
            UndertowContainer server = (UndertowContainer) container;
            if (isNotDeployed()) {
                DeploymentInfo webapp = getDeployment();
                manager = Servlets.defaultContainer().addDeployment(webapp);
                manager.deploy();
                path = Handlers.path(Handlers.redirect(server.getContextPath())).addPrefixPath(container.getContextPath(), manager.start());
            }
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

            path = null;
        } catch (ServletException e) {
            throw new DescopedServerException(e);
        }


        // do something
//        Container.instance().cleanup();
//        Container.instance().setState(ContainerState.DISCOVERED);
    }

}
