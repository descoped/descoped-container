package io.descoped.container.jetty.core;

import io.descoped.container.core.ServerContainer;
import io.descoped.container.exception.DescopedServerException;
import org.eclipse.jetty.server.Server;

/**
 * Created by oranheim on 03/02/2017.
 */
public class JettyContainer extends ServerContainer {

    private Server server;

    public JettyContainer() {
        server = new Server(getPort());
    }

    public Server builder() {
        return server;
    }

    @Override
    public void start() {
        try {
            server.start();
        } catch (Exception e) {
            throw new DescopedServerException("Error starting Jetty Server", e);
        }
    }

    @Override
    public void shutdown() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new DescopedServerException("Error starting Jetty Server", e);
        }
    }

    @Override
    public boolean isRunning() {
        return server.isRunning();
    }

    @Override
    public boolean isStopped() {
        return server.isStopped();
    }

}
