package io.descoped.server.container;

import io.descoped.server.deployment.RestDeployment;
import io.undertow.Undertow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Vetoed;
import javax.enterprise.inject.spi.CDI;

/**
 * Created by oranheim on 12/12/2016.
 */
@Vetoed
public class UndertowContainer extends ServerContainer {

    private static final Logger log = LoggerFactory.getLogger(UndertowContainer.class);
    private Undertow server;
    private Undertow.Builder builder;

    public UndertowContainer() {
        super();
    }

    private String contextPath() {
        String ctxPath = this.getContextPath();
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
        deploy(deployment);
    }

    @Override
    public void start() {
        if (!isRunning() && isStopped()) {
            CDI.current().getBeanManager().fireEvent(new PreStartContainer(this));

            if (getDeployments().isEmpty()) {
                log.error("\n\n\t-----> Unable to start Descoped Server because no Deployment has been registered! <-----\n");
                System.exit(-1);
            }

            server = builder.build();

            if (getMaxWorkers() > 0) {
                builder.setWorkerThreads(getMaxWorkers());
            }

            server.start();
        }
    }

    @Override
    public void shutdown() {
        if (isRunning() && !isStopped()) {
            CDI.current().getBeanManager().fireEvent(new PreStopContainer(this));
            for (Deployment deployment : getDeployments()) {
                undeploy(deployment);
            }
            getDeployments().clear();
            server.stop();
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
