package io.descoped.server.container;

import io.descoped.server.Main;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import org.apache.deltaspike.cdise.servlet.CdiServletRequestListener;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.ws.rs.core.Application;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by oranheim on 12/12/2016.
 */
public class UndertowContainer extends ServerContainer {

    private static final Logger log = LoggerFactory.getLogger(UndertowContainer.class);
    private static PathHandler path;
    private Undertow server;
    private Undertow.Builder builder;

    public UndertowContainer(ServerContainer owner) {
        super(owner);
    }

    private List<ServletContainerInitializerInfo> getServletContainerInitializers() {
        List<ServletContainerInitializerInfo> infos = new ArrayList<>();
        for (ServletContainerInitializer initializer : ServiceLoader.load(ServletContainerInitializer.class)) {
            HandlesTypes types = initializer.getClass().getAnnotation(HandlesTypes.class);
            infos.add(new ServletContainerInitializerInfo(
                    initializer.getClass(),
                    new ImmediateInstanceFactory<>(initializer),
                    types == null ? Collections.emptySet() : new LinkedHashSet<>(Arrays.asList(types.value()))));
        }
        return infos;
    }

    private static String contextPath() {
        String ctxPath = ServerContainer.getContextPath();
        if (!ctxPath.startsWith("/")) {
            ctxPath = "/" + ctxPath;
        }
        return ctxPath;
    }

    private static void preparePathHandler() throws ServletException {
        ListenerInfo listenerInfo = Servlets.listener(CdiServletRequestListener.class);

        DeploymentInfo webapp = Servlets.deployment()
                .addListener(listenerInfo)
//                    .addServletContainerInitalizers(getServletContainerInitializers())
                .setClassLoader(ClassLoader.getSystemClassLoader())
                .setContextPath(getContextPath())
                .setDefaultEncoding(StandardCharsets.UTF_8.displayName())
                .setDeploymentName(Main.class.getSimpleName())
                .setDisplayName(Main.class.getSimpleName())
                .setEagerFilterInit(true);;

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

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(webapp);
        manager.deploy();

        path = Handlers.path(Handlers.redirect(contextPath())).addPrefixPath(contextPath(), manager.start());

    }

    private Undertow createHttpServer() throws ServletException {
        try {
            if (path == null) preparePathHandler();
            builder = Undertow.builder()
                    .addHttpListener(getPort(), getHost())
                    .setHandler(path);

//        if (getMaxWorkers() > 0) {
//            builder.setWorkerThreads(getMaxWorkers());
//        }

            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        if (!isRunning() && isStopped()) {
            try {
                CDI.current().getBeanManager().fireEvent(new ApplicationStartupEvent());
                server = createHttpServer();
                server.start();
            } catch (ServletException e) {
                log.error("Error starting server: {}", e);
            }
        }
    }

    @Override
    public void shutdown() {
        if (isRunning() && !isStopped()) {
            server.stop();
//            for(String dep : Servlets.defaultContainer().listDeployments()) {
//                DeploymentManager di = Servlets.defaultContainer().getDeployment(dep);
//                try {
//                    di.stop();
//                    di.undeploy();
//                } catch (ServletException e) {
//                    e.printStackTrace();
//                }
//            }
//            Servlets.deployment().getListeners().clear();
//            Handlers.path().clearPaths();
            server = null;
        }
    }

    @Override
    public boolean isRunning() {
        return server != null;
    }

    @Override
    public boolean isStopped() {
        return server == null;
    }

}
