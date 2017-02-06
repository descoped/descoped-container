package io.descoped.container.undertow.exception;

/**
 * Created by oranheim on 02/02/2017.
 */

import io.descoped.container.undertow.core.UndertowContainer;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

/**
 * ServerException used by exception handler
 */
public class ServerException extends Exception {

    public static final ServerException NOT_FOUND = new ServerException(StatusCodes.NOT_FOUND);
    public static final ServerException BAD_REQUEST = new ServerException(StatusCodes.BAD_REQUEST);
    public static final ServerException METHOD_NOT_ALLOWED = new ServerException(StatusCodes.METHOD_NOT_ALLOWED);

    private final int code;

    public ServerException(int code) {
        super(StatusCodes.getReason(code));
        this.code = code;
    }

    public ServerException(int code, String message) {
        super(StatusCodes.getReason(code) + ":" + message);
        this.code = code;
    }

    public void serveError(HttpServerExchange exchange) {
        exchange.setResponseCode(code);

        StringBuilder out = new StringBuilder();
        errorText(out);
        UndertowContainer.log.error("serveError: {}", out.toString());
        exchange.getResponseSender().send(out.toString());
    }

    public void errorText(StringBuilder out) {
        out.append(getMessage());
    }
}