package io.descoped.container.undertow.deployment;

import io.descoped.container.core.ServerContainer;
import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.undertow.core.UndertowContainer;
import io.descoped.container.undertow.exception.ServerException;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

/**
 * Created by oranheim on 20/12/2016.
 */
public abstract class UndertowDeployment extends BaseDeployment {

    private static final Logger log = LoggerFactory.getLogger(UndertowDeployment.class);

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

    protected boolean isAskingForJson(HttpServerExchange exchange) {
        HeaderValues values = exchange.getRequestHeaders().get(Headers.ACCEPT);
        return values != null && values.getFirst().contains("application/json");
    }

    protected String createMessage(String message, boolean json) {
        return json ? "{ \"message\": \"" + message + "\"}" : message;
    }

    protected HttpHandler exceptionHandler(HttpHandler next) {
        ExceptionHandler exceptionHandler = Handlers.exceptionHandler(next);
        exceptionHandler.addExceptionHandler(ServerException.class, (exchange) -> {
            Throwable exception = exchange.getAttachment(ExceptionHandler.THROWABLE);
            if (exception instanceof ServerException) {
                ((ServerException) exception).serveError(exchange);
            } else {
                // TODO create real error message
                String message = exception.getMessage();
                exchange.setResponseCode(StatusCodes.INTERNAL_SERVER_ERROR);
                exchange.getResponseSender().send(createMessage(message, isAskingForJson(exchange)));
            }
        });
        return exceptionHandler;
    }

    protected HttpHandler log(HttpHandler next) {
        return (exchange) -> {
            long start = System.currentTimeMillis();
            next.handleRequest(exchange);
            long end = System.currentTimeMillis();
            long time = end - start < 0 ? 0 : end - start;
            UndertowContainer.log.info("" + exchange.getRequestMethod() + " " + exchange.getRequestHeaders().get(Headers.ACCEPT) + " "
                    + exchange.getRequestURI() /*+ " with code " + exchange.getResponseCode()*/ + " in " + time + " ms");
        };
    }

    @Override
    public void deploy(ServerContainer container) {
        try {
            UndertowContainer server = (UndertowContainer) container;
            UndertowWebApp deployment = deployment(server);
            manager = Servlets.defaultContainer().addDeployment(deployment.getDeploymentInfo());
            manager.deploy();

            path = Handlers.path(Handlers.redirect(server.getContextPath())).addPrefixPath(container.getContextPath(), manager.start());
            HttpHandler rootHandler;
            if (true) {
                HttpHandler exceptionHandler = exceptionHandler(path);
                HttpHandler logHandler = log(exceptionHandler);

                rootHandler = Handlers.gracefulShutdown(logHandler);
//            HttpHandler rootHandler = (HttpHandler) Proxy.newProxyInstance(ClassLoaders.tccl(), new Class[]{HttpHandler.class}, new HttpHandlerProxy(gracefulShutdownHandler));
            } else {
                rootHandler = path;
            }

            server.builder().setHandler(rootHandler);

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
