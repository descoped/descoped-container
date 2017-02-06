package io.descoped.container.tomcat.deployment;

import io.descoped.container.deployment.spi.Servlet;
import io.descoped.container.deployment.spi.WebApp;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

/**
 * Created by oranheim on 03/02/2017.
 */
public class TomcatServlet implements Servlet<TomcatWebApp> {

    private final WebApp<TomcatWebApp> owner;
    private final String servletName;
    private final Class<? extends javax.servlet.Servlet> servlet;
    protected static ThreadLocal<Integer> servletCount = ThreadLocal.withInitial(() -> 0);
    private final Wrapper servletWrapper;

    public TomcatServlet(WebApp<TomcatWebApp> owner, String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        this.owner = owner;
        this.servletName = servletName;
        this.servlet = servlet;
        owner().infoBuilder.key("addServlet" + servletCount.get())
                .keyValue("name", servletName)
                .keyValue("servlet", servlet.getName())
                ;
        servletWrapper = Tomcat.addServlet(owner().getStandardContext(), this.servletName, this.servlet.getName());
    }

    private TomcatWebApp owner() {
        return (TomcatWebApp) owner;
    }

    @Override
    public TomcatWebApp up() {
        owner().infoBuilder.up();
        servletCount.set(servletCount.get()+1);
        return owner();
    }

    public Wrapper getServletWrapper() {
        return servletWrapper;
    }

    @Override
    public Servlet<TomcatWebApp> addMapping(String mapping) {
        owner().infoBuilder.keyValue("mapping", mapping);
        servletWrapper.addMapping(mapping);
        return this;
    }

    @Override
    public Servlet<TomcatWebApp> addInitParam(String name, String value) {
        owner().infoBuilder.key("initParam")
                .keyValue("name", name)
                .keyValue("value", value)
                .up();
        servletWrapper.addInitParameter(name, value);
        return this;
    }

    @Override
    public Servlet<TomcatWebApp> setAsyncSupported(boolean asyncSupported) {
        owner().infoBuilder.keyValue("asyncSupported", String.valueOf(asyncSupported));
        servletWrapper.setAsyncSupported(asyncSupported);
        return this;
    }

    @Override
    public Servlet<TomcatWebApp> setLoadOnStartup(int loadOnStartup) {
        owner().infoBuilder.keyValue("loadOnStartup", String.valueOf(loadOnStartup));
        servletWrapper.setLoadOnStartup(loadOnStartup);
        return this;
    }

    @Override
    public Servlet<TomcatWebApp> setEnabled(boolean enabled) {
        owner().infoBuilder.keyValue("enabled", String.valueOf(enabled));
        servletWrapper.setEnabled(enabled);
        return this;
    }
}
