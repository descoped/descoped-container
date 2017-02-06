package io.descoped.container.grizzly.core;

import io.descoped.container.core.ServerContainer;
import io.descoped.container.exception.DescopedServerException;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by oranheim on 03/02/2017.
 */
public class GrizzlyContainer extends ServerContainer {

    private final static Logger log = LoggerFactory.getLogger(GrizzlyContainer.class);
    private final HttpServer server;

    public GrizzlyContainer() {
        try {
            URI uri = new URI("http", null, getHost(), getPort(), null, null, null);
            log.trace("Bind to URL: {}", uri.toString());
            server = GrizzlyHttpServerFactory.createHttpServer(uri, false);

        } catch (URISyntaxException e) {
            throw new DescopedServerException(e);
        }

    }

    public HttpServer builder() {
        return server;
    }

    @Override
    public void start() {
        try {
            log.trace("Starting Grizzly..");
            server.start();
            log.trace("Started Grizzly");
        } catch (Exception e) {
            throw new DescopedServerException("Error starting Grizzly Server", e);
        }
    }

    @Override
    public void shutdown() {
        try {
            log.trace("Stopping Grizzly..");
            server.shutdown();
            log.trace("Stopped Grizzly");
        } catch (Exception e) {
            throw new DescopedServerException("Error starting Grizzly Server", e);
        }
    }

    @Override
    public boolean isRunning() {
        return server.isStarted();
    }

    @Override
    public boolean isStopped() {
        return !server.isStarted();
    }

}
