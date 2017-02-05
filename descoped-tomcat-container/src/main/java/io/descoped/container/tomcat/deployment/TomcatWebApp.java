package io.descoped.container.tomcat.deployment;

import io.descoped.container.deployment.spi.Filter;
import io.descoped.container.deployment.spi.Servlet;
import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.info.InfoBuilder;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by oranheim on 03/02/2017.
 */
public class TomcatWebApp implements WebApp<TomcatWebApp> {

//    private WebAppContext webAppContext;
    private List<String> welcomePages = new ArrayList<>();
    protected static ThreadLocal<Integer> listenerCount = ThreadLocal.withInitial(() -> 0);
    protected InfoBuilder infoBuilder = InfoBuilder.builder();

    public TomcatWebApp() {
        TomcatWebApp.listenerCount.remove();
        TomcatServlet.servletCount.remove();
        TomcatFilter.filterCount.remove();

//        webAppContext = new WebAppContext();
//        webAppContext.setClassLoader(ClassLoaders.tccl());
//        webAppContext.setResourceBase(".");
    }

//    public WebAppContext getWebAppContext() {
//        return webAppContext;
//    }

    @Override
    public TomcatWebApp name(String name) {
        infoBuilder.keyValue("name", name);
//        webAppContext.setDisplayName(name);
        return this;
    }

    @Override
    public TomcatWebApp contextPath(String contextPath) {
        infoBuilder.keyValue("contextPath", contextPath);
//        webAppContext.setContextPath(contextPath);
        return this;
    }

    @Override
    public TomcatWebApp addListener(Class<? extends EventListener> listenerClass) {
        infoBuilder.keyValue("addListener" + listenerCount.get(), listenerClass.getName()); listenerCount.set(listenerCount.get().intValue()+1);
//        try {
//            webAppContext.addEventListener(listenerClass.newInstance());
//        } catch (IllegalAccessException | InstantiationException e) {
//            throw new DescopedServerException(e);
//        }
        return this;
    }

    @Override
    public Filter<TomcatWebApp> addFilter(String filterName, Class<? extends javax.servlet.Filter> filterClass) {
        return new TomcatFilter(this, filterName, filterClass);
    }

    @Override
    public Servlet<TomcatWebApp> addServlet(String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        return new TomcatServlet(this, servletName, servlet);
    }

    @Override
    public TomcatWebApp setEagerFilterInit(boolean eagerFilterInit) {
        //infoBuilder.keyValue("eagerFilterInit", String.valueOf(eagerFilterInit));
        // do nothing - not supported jetty
        return this;
    }

    @Override
    public TomcatWebApp addWelcomePage(String welcomePage) {
        infoBuilder.keyValue("welcomePage", welcomePage);
        welcomePages.add(welcomePage);
//        webAppContext.setWelcomeFiles(welcomePages.toArray(new String[welcomePages.size()]));
        return this;
    }

    @Override
    public String info() {
        return infoBuilder.build();
    }

    @Override
    public TomcatWebApp build() {
        return this;
    }
}
