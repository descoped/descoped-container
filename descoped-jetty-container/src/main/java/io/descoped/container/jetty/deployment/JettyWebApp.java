package io.descoped.container.jetty.deployment;

import io.descoped.container.deployment.spi.Filter;
import io.descoped.container.deployment.spi.Servlet;
import io.descoped.container.deployment.spi.WebApp;

import java.util.EventListener;

/**
 * Created by oranheim on 03/02/2017.
 */
public class JettyWebApp implements WebApp<JettyWebApp> {

    @Override
    public JettyWebApp name(String name) {
        return null;
    }

    @Override
    public JettyWebApp contextPath(String contextPath) {
        return null;
    }

    @Override
    public JettyWebApp addListener(Class<? extends EventListener> listenerClass) {
        return null;
    }

    @Override
    public Filter<JettyWebApp> addFilter(String filterName, Class<? extends javax.servlet.Filter> filterClass) {
        return null;
    }

    @Override
    public Servlet<JettyWebApp> addServlet(String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        return null;
    }

    @Override
    public JettyWebApp setEagerFilterInit(boolean eagerFilterInit) {
        return null;
    }

    @Override
    public JettyWebApp addWelcomePage(String welcomePage) {
        return null;
    }

    @Override
    public String info() {
        return null;
    }

    @Override
    public JettyWebApp build() {
        return null;
    }
}
