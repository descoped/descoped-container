package io.descoped.container.jetty.deployment;

import io.descoped.container.deployment.spi.Servlet;
import io.descoped.container.deployment.spi.WebApp;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by oranheim on 03/02/2017.
 */
public class JettyServlet implements Servlet<JettyWebApp> {

    private final WebApp<JettyWebApp> owner;
    private final String servletName;
    private final Class<? extends javax.servlet.Servlet> servlet;
    private final ServletHolder servletHolder;

    public JettyServlet(WebApp<JettyWebApp> owner, String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        this.owner = owner;
        this.servletName = servletName;
        this.servlet = servlet;
        this.servletHolder = owner().getWebAppContext().addServlet(this.servlet, owner().getWebAppContext().getContextPath());
        servletHolder.setDisplayName(this.servletName);
    }

    private JettyWebApp owner() {
        return (JettyWebApp) owner;
    }

    @Override
    public JettyWebApp up() {
        return owner();
    }

    @Override
    public Servlet<JettyWebApp> addMapping(String mapping) {
        // do nothing - already set in servletHolder
        return this;
    }

    @Override
    public Servlet<JettyWebApp> addInitParam(String name, String value) {
        servletHolder.setInitParameter(name, value);
        return this;
    }

    @Override
    public Servlet<JettyWebApp> setAsyncSupported(boolean asyncSupported) {
        servletHolder.setAsyncSupported(asyncSupported);
        return this;
    }

    @Override
    public Servlet<JettyWebApp> setLoadOnStartup(int loadOnStartup) {
        servletHolder.setInitOrder(loadOnStartup);
        return this;
    }

    @Override
    public Servlet<JettyWebApp> setEnabled(boolean enabled) {
        servletHolder.setEnabled(enabled);
        return this;
    }
}
