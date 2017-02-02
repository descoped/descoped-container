package io.descoped.container.jetty.deployment;

import io.descoped.container.deployment.spi.Servlet;

/**
 * Created by oranheim on 03/02/2017.
 */
public class JettyServlet implements Servlet<JettyWebApp> {
    @Override
    public JettyWebApp up() {
        return null;
    }

    @Override
    public Servlet<JettyWebApp> addMapping(String mapping) {
        return null;
    }

    @Override
    public Servlet<JettyWebApp> addInitParam(String name, String value) {
        return null;
    }

    @Override
    public Servlet<JettyWebApp> setAsyncSupported(boolean asyncSupported) {
        return null;
    }

    @Override
    public Servlet<JettyWebApp> setLoadOnStartup(int loadOnStartup) {
        return null;
    }

    @Override
    public Servlet<JettyWebApp> setEnabled(boolean asyncSupported) {
        return null;
    }
}
