package io.descoped.container.tomcat.core;

import io.descoped.container.core.ServerContainer;
import io.descoped.container.exception.DescopedServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by oranheim on 03/02/2017.
 */
public class TomcatContainer extends ServerContainer {

    private final static Logger log = LoggerFactory.getLogger(TomcatContainer.class);

    public TomcatContainer() {
//        server = new Server(getPort());
    }

//    public Server builder() {
//        return server;
//    }

    @Override
    public void start() {
        try {
            log.trace("Starting Tomcat..");
//            server.start();
            log.trace("Started Tomcat");
//            server.join();
        } catch (Exception e) {
            throw new DescopedServerException("Error starting Tomcat Server", e);
        }
    }

    @Override
    public void shutdown() {
        try {
            log.trace("Stopping Tomcat..");
//            server.stop();
            log.trace("Stopped Tomcat");
        } catch (Exception e) {
            throw new DescopedServerException("Error starting Tomcat Server", e);
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
