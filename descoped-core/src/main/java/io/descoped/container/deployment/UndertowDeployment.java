package io.descoped.container.deployment;

import io.descoped.container.core.ServerContainer;
import io.descoped.container.core.UndertowContainer;
import io.descoped.container.support.DescopedServerException;
import io.undertow.Handlers;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentManager;

import javax.servlet.ServletException;

/**
 * Created by oranheim on 20/12/2016.
 */
public abstract class UndertowDeployment extends BaseDeployment {

    private DeploymentManager manager;
    private PathHandler path;

    public UndertowDeployment() {
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
            UndertowWebApp deployment = deployment(server);
            manager = Servlets.defaultContainer().addDeployment(deployment.getDeploymentInfo());
            manager.deploy();
            path = Handlers.path(Handlers.redirect(server.getContextPath())).addPrefixPath(container.getContextPath(), manager.start());
            server.builder().setHandler(path);
        } catch (ServletException e) {
            throw new DescopedServerException(e);
        }
    }

    abstract public UndertowWebApp deployment(UndertowContainer container);

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


}
