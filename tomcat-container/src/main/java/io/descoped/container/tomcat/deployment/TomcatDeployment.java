package io.descoped.container.tomcat.deployment;

import io.descoped.container.core.Deployment;
import io.descoped.container.core.ServerContainer;
import io.descoped.container.tomcat.core.TomcatContainer;

/**
 * Created by oranheim on 03/02/2017.
 */
public class TomcatDeployment implements Deployment {

    private TomcatWebApp webapp;

    public void register(TomcatWebApp webapp) {
        this.webapp = webapp;
    }

    @Override
    public void deploy(ServerContainer container) {
        TomcatContainer server = (TomcatContainer) container;

    }

    @Override
    public void undeploy(ServerContainer container) {
        TomcatContainer server = (TomcatContainer) container;
//        server.builder().getHandler().destroy();
    }

}
