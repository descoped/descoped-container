package io.descoped.container.grizzly.deployment;

import io.descoped.container.deployment.spi.Filter;
import io.descoped.container.deployment.spi.Servlet;
import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.info.InfoBuilder;
import org.glassfish.grizzly.servlet.WebappContext;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by oranheim on 03/02/2017.
 */
public class GrizzlyWebApp implements WebApp<GrizzlyWebApp> {

    private List<String> welcomePages = new ArrayList<>();
    protected static ThreadLocal<Integer> listenerCount = ThreadLocal.withInitial(() -> 0);
    protected InfoBuilder infoBuilder = InfoBuilder.builder();
    private WebappContext webappContext;
    private String displayName;

    public GrizzlyWebApp() {
        GrizzlyWebApp.listenerCount.remove();
        GrizzlyServlet.servletCount.remove();
        GrizzlyFilter.filterCount.remove();
    }

    public WebappContext getWebAppContext() {
        if (webappContext == null && displayName == null) {
            throw new DescopedServerException("You must specify GrizzlyWebApp.name() as your FIRST declaration in your deployment!");
        } else if (webappContext == null) {
            throw new DescopedServerException("You must specify GrizzlyWebApp.contextPath() as your SECOND declaration in your deployment!");
        }
        return webappContext;
    }

    @Override
    public GrizzlyWebApp name(String name) {
        infoBuilder.keyValue("name", name);
        this.displayName = name;
        return this;
    }

    @Override
    public GrizzlyWebApp contextPath(String contextPath) {
        infoBuilder.keyValue("contextPath", contextPath);
        webappContext = new WebappContext(displayName, contextPath);
        return this;
    }

    @Override
    public GrizzlyWebApp addListener(Class<? extends EventListener> listenerClass) {
        infoBuilder.keyValue("addListener" + listenerCount.get(), listenerClass.getName()); listenerCount.set(listenerCount.get().intValue()+1);
        getWebAppContext().addListener(listenerClass);
        return this;
    }

    @Override
    public Filter<GrizzlyWebApp> addFilter(String filterName, Class<? extends javax.servlet.Filter> filterClass) {
        return new GrizzlyFilter(this, filterName, filterClass);
    }

    @Override
    public Servlet<GrizzlyWebApp> addServlet(String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        return new GrizzlyServlet(this, servletName, servlet);
    }

    @Override
    public GrizzlyWebApp setEagerFilterInit(boolean eagerFilterInit) {
        //infoBuilder.keyValue("eagerFilterInit", String.valueOf(eagerFilterInit));
        // do nothing - not supported jetty
        return this;
    }

    @Override
    public GrizzlyWebApp addWelcomePage(String welcomePage) {
//        infoBuilder.keyValue("welcomePage", welcomePage);
//        welcomePages.add(welcomePage);
        // do nothing - there is no concept of welcome pages in Grizzly
        return this;
    }

    @Override
    public String info() {
        return infoBuilder.build();
    }

    @Override
    public GrizzlyWebApp build() {
        return this;
    }
}
