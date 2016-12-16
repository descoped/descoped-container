package io.descoped.server.container;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by oranheim on 12/12/2016.
 */
public class GrizzlyContainer extends ServerContainer {

    private static final Logger log = LoggerFactory.getLogger(GrizzlyContainer.class);
    private HttpServer server;

    public GrizzlyContainer(ServerContainer owner) {
        super(owner);
    }

    private ResourceConfig createResourceConfig() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // create JsonProvider to provide custom ObjectMapper
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(mapper);

        // configure REST service
        ResourceConfig rc = new ResourceConfig().packages(getJaxRsPackages());
        rc.register(provider);
        return rc;
    }

    private HttpServer createHttpServer() {
        try {
            Application rc = createResourceConfig();

            // create Grizzly instance and add handler
            HttpHandler handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, rc);
            URI uri = new URI("http", null, getHost(), getPort(), null, null, null);
            log.trace("Bind to URL: {}", uri.toString());

            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, false);
            ServerConfiguration config = server.getServerConfiguration();

            config.addHttpHandler(handler, "/");
            return server;
        } catch (URISyntaxException e) {
            log.error("Unable to create HTTP server! {}", e);
        }
        return null;
    }


    @Override
    public void start() {
        if (!isRunning() && isStopped()) {
            server = createHttpServer();

            // todo: https://github.com/jersey/jersey/tree/master/containers
            {
                //ListenerInfo listenerInfo = Servlets.listener(CdiServletRequestListener.class);
//                WebappContext context = new WebappContext(Main.class.getSimpleName(), getContextPath());
//                context.addListener(CdiServletRequestListener.class);
//                context.deploy(server);
            }

                try {
                server.start();
            } catch (IOException e) {
                log.error("Error starting server: {}", e);
            }
        }
    }

    @Override
    public void shutdown() {
        if (isRunning() && !isStopped()) {
            server.shutdown();
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
