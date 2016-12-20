package io.descoped.server.container;

import io.descoped.server.deployment.RestDeployment;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.spi.CDI;

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

    private static String contextPath() {
        String ctxPath = ServerContainer.getContextPath();
        if (!ctxPath.startsWith("/")) {
            ctxPath = "/" + ctxPath;
        }
        return ctxPath;
    }

    public Undertow.Builder builder() {
        if (builder == null) {
            builder = Undertow.builder()
                .addHttpListener(getPort(), getHost())
                ;
        }
        return builder;
    }

    public void deployJaxRsResourceConfig() {
        RestDeployment deployment = new RestDeployment();
        deployment.deploy(this);
    }


    @Override
    public void start() {
        if (!isRunning() && isStopped()) {
            CDI.current().getBeanManager().fireEvent(new ApplicationStartupEvent(this));
            server = builder.build();

//        if (getMaxWorkers() > 0) {
//            builder.setWorkerThreads(getMaxWorkers());
//        }

            server.start();
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
