package io.descoped.container.grizzly.core;

import io.descoped.container.core.ServerContainer;
import io.descoped.container.exception.DescopedServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by oranheim on 03/02/2017.
 */
public class GrizzlyContainer extends ServerContainer {

    private final static Logger log = LoggerFactory.getLogger(GrizzlyContainer.class);

    public GrizzlyContainer() {
//        server = new Server(getPort());
    }

//    public Server builder() {
//        return server;
//    }

    @Override
    public void start() {
        try {
            log.trace("Starting Grizzly..");
//            server.start();
            log.trace("Started Grizzly");
//            server.join();
        } catch (Exception e) {
            throw new DescopedServerException("Error starting Grizzly Server", e);
        }
    }

    @Override
    public void shutdown() {
        try {
            log.trace("Stopping Grizzly..");
//            server.stop();
            log.trace("Stopped Grizzly");
        } catch (Exception e) {
            throw new DescopedServerException("Error starting Grizzly Server", e);
        }
    }

    @Override
    public boolean isRunning() {
//        return server.isRunning();
        return false;
    }

    @Override
    public boolean isStopped() {
//        return server.isStopped();
        return false;
    }

}
