package io.descoped.container.undertow.deployment;

import io.undertow.server.handlers.GracefulShutdownHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by oranheim on 02/02/2017.
 */
public class HttpHandlerProxy implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(HttpHandlerProxy.class);
    private final GracefulShutdownHandler gracefulShutdownHandler;

    public HttpHandlerProxy(GracefulShutdownHandler gracefulShutdownHandler) {
        this.gracefulShutdownHandler = gracefulShutdownHandler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.trace("gracefulShutdownHandler: {} -- proxy: {} -- method: {} -- args: {}", gracefulShutdownHandler, proxy.getClass(), method, args);
        if (proxy instanceof Proxy) {
            //This is a proxy
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
            return invocationHandler.invoke(proxy, method, args);
        } else {
            // This is not a proxy
            return method.invoke(gracefulShutdownHandler, args);
        }
//        return null;
    }

}
