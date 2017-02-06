package io.descoped.container.grizzly.deployment;

import io.descoped.container.core.Deployment;
import io.descoped.container.core.ServerContainer;
import io.descoped.container.grizzly.core.GrizzlyContainer;
import org.glassfish.grizzly.http.server.ServerConfiguration;

/**
 * Created by oranheim on 03/02/2017.
 */
public class GrizzlyDeployment implements Deployment {

    private GrizzlyWebApp webapp;

    public void register(GrizzlyWebApp webapp) {
        this.webapp = webapp;
    }

    @Override
    public void deploy(ServerContainer container) {
        GrizzlyContainer server = (GrizzlyContainer) container;
        ServerConfiguration configuration = server.builder().getServerConfiguration();
        webapp.getWebAppContext().deploy(server.builder());
    }

    @Override
    public void undeploy(ServerContainer container) {
        GrizzlyContainer server = (GrizzlyContainer) container;
        server.builder().getHttpHandler().destroy();
    }

}
