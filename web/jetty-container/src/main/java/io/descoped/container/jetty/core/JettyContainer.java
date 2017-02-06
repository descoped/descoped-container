package io.descoped.container.jetty.core;

import io.descoped.container.core.ServerContainer;
import io.descoped.container.exception.DescopedServerException;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by oranheim on 03/02/2017.
 */
public class JettyContainer extends ServerContainer {

    private final static Logger log = LoggerFactory.getLogger(JettyContainer.class);
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
            log.trace("Starting Jetty..");
            server.start();
            log.trace("Started Jetty");
//            server.join();
        } catch (Exception e) {
            throw new DescopedServerException("Error starting Jetty Server", e);
        }
    }

    @Override
    public void shutdown() {
        try {
            log.trace("Stopping Jetty..");
            server.stop();
            log.trace("Stopped Jetty");
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
