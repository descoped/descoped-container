package io.descoped.container.tomcat.deployment;

import io.descoped.container.deployment.spi.Filter;
import io.descoped.container.deployment.spi.Servlet;
import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.info.InfoBuilder;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardWrapper;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by oranheim on 03/02/2017.
 */
public class TomcatWebApp implements WebApp<TomcatWebApp> {

    protected static ThreadLocal<Integer> listenerCount = ThreadLocal.withInitial(() -> 0);
    protected InfoBuilder infoBuilder = InfoBuilder.builder();
    private final StandardContext webAppContext;
    private final StandardWrapper wrapper;
    private final List<TomcatServlet> servletList = new ArrayList<>();

    /*
      Usefull ref:  http://www.artificialworlds.net/blog/2015/02/05/programmatic-equivalents-of-web-xml-sections-for-tomcat/
     */

    public TomcatWebApp() {
        TomcatWebApp.listenerCount.remove();
        TomcatServlet.servletCount.remove();
        TomcatFilter.filterCount.remove();
        
        webAppContext = new StandardContext();
//        webAppContext.setClassLoader(ClassLoaders.tccl());
//        webAppContext.setResourceBase(".");
//        wrapper = new StandardWrapper();
        wrapper = (StandardWrapper) webAppContext.createWrapper();
    }

    public StandardContext getStandardContext() {
        return webAppContext;
    }

    public StandardWrapper getStandardWrapper() {
        return wrapper;
    }

    public List<TomcatServlet> getServlets() {
        return servletList;
    }

    @Override
    public TomcatWebApp name(String name) {
        infoBuilder.keyValue("name", name);
        webAppContext.setName(name);
        return this;
    }

    @Override
    public TomcatWebApp contextPath(String contextPath) {
        infoBuilder.keyValue("contextPath", contextPath);
        if ("/".equals(contextPath)) contextPath = "";
        webAppContext.setPath(contextPath);
        return this;
    }

    @Override
    public TomcatWebApp addListener(Class<? extends EventListener> listenerClass) {
        infoBuilder.keyValue("addListener" + listenerCount.get(), listenerClass.getName()); listenerCount.set(listenerCount.get().intValue()+1);
        webAppContext.addApplicationListener(listenerClass.getName());
        return this;
    }

    @Override
    public Filter<TomcatWebApp> addFilter(String filterName, Class<? extends javax.servlet.Filter> filterClass) {
        return new TomcatFilter(this, filterName, filterClass);
    }

    @Override
    public Servlet<TomcatWebApp> addServlet(String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        TomcatServlet servletHolder = new TomcatServlet(this, servletName, servlet);
        servletList.add(servletHolder);
        return servletHolder;
    }

    @Override
    public TomcatWebApp setEagerFilterInit(boolean eagerFilterInit) {
        // do nothing - not supported tomcat
        return this;
    }

    @Override
    public TomcatWebApp addWelcomePage(String welcomePage) {
        infoBuilder.keyValue("welcomePage", welcomePage);
        webAppContext.addWelcomeFile(welcomePage);
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
