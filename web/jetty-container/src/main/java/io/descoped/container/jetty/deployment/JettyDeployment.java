package io.descoped.container.jetty.deployment;

import io.descoped.container.core.Deployment;
import io.descoped.container.core.ServerContainer;
import io.descoped.container.extension.ClassLoaders;
import io.descoped.container.jetty.core.JettyContainer;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Created by oranheim on 03/02/2017.
 */
public class JettyDeployment implements Deployment {

    private JettyWebApp webapp;

    public void register(JettyWebApp webapp) {
        this.webapp = webapp;
    }

    @Override
    public void deploy(ServerContainer container) {
        JettyContainer server = (JettyContainer) container;

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath(server.getContextPath());
        context.setClassLoader(ClassLoaders.tccl());
        context.setResourceBase(".");

//        context.addEventListener(new ContextLoaderListener());
//        context.setInitParameter("contextConfigLocation", "classpath:context.xml");

//        ServerConnector connector = new ServerConnector(server.builder());
//        connector.setPort(server.getPort());
//        server.builder().addConnector(connector);

        context.setHandler(webapp.getWebAppContext());

        server.builder().setHandler(context);
    }

    @Override
    public void undeploy(ServerContainer container) {
        JettyContainer server = (JettyContainer) container;
        server.builder().getHandler().destroy();
    }

}
