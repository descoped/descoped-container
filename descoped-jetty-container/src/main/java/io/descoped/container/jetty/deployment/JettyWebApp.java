package io.descoped.container.jetty.deployment;

import io.descoped.container.deployment.spi.Filter;
import io.descoped.container.deployment.spi.Servlet;
import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.extension.ClassLoaders;
import org.eclipse.jetty.webapp.WebAppContext;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by oranheim on 03/02/2017.
 */
public class JettyWebApp implements WebApp<JettyWebApp> {

    private WebAppContext webAppContext;
    private List<String> welcomePages = new ArrayList<>();

    public JettyWebApp() {
        webAppContext = new WebAppContext();
        webAppContext.setClassLoader(ClassLoaders.tccl());
        webAppContext.setResourceBase(".");
    }

    public WebAppContext getWebAppContext() {
        return webAppContext;
    }

    @Override
    public JettyWebApp name(String name) {
        webAppContext.setDisplayName(name);
        return this;
    }

    @Override
    public JettyWebApp contextPath(String contextPath) {
        webAppContext.setContextPath(contextPath);
        return this;
    }

    @Override
    public JettyWebApp addListener(Class<? extends EventListener> listenerClass) {
        try {
            webAppContext.addEventListener(listenerClass.newInstance());
        } catch (IllegalAccessException | InstantiationException e) {
            throw new DescopedServerException(e);
        }
        return this;
    }

    @Override
    public Filter<JettyWebApp> addFilter(String filterName, Class<? extends javax.servlet.Filter> filterClass) {
        return new JettyFilter(this, filterName, filterClass);
    }

    @Override
    public Servlet<JettyWebApp> addServlet(String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        return new JettyServlet(this, servletName, servlet);
    }

    @Override
    public JettyWebApp setEagerFilterInit(boolean eagerFilterInit) {
        // do nothing - not supported jetty
        return this;
    }

    @Override
    public JettyWebApp addWelcomePage(String welcomePage) {
        welcomePages.add(welcomePage);
        webAppContext.setWelcomeFiles(welcomePages.toArray(new String[welcomePages.size()]));
        return this;
    }

    @Override
    public String info() {
        return "";
    }

    @Override
    public JettyWebApp build() {
        return this;
    }
}
