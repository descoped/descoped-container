package io.descoped.container.tomcat.deployment;

import io.descoped.container.deployment.spi.Servlet;
import io.descoped.container.deployment.spi.WebApp;

/**
 * Created by oranheim on 03/02/2017.
 */
public class TomcatServlet implements Servlet<TomcatWebApp> {

    private final WebApp<TomcatWebApp> owner;
    private final String servletName;
    private final Class<? extends javax.servlet.Servlet> servlet;
    protected static ThreadLocal<Integer> servletCount = ThreadLocal.withInitial(() -> 0);

    public TomcatServlet(WebApp<TomcatWebApp> owner, String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        this.owner = owner;
        owner().infoBuilder.key("addServlet" + servletCount.get())
                .keyValue("name", servletName)
                .keyValue("servlet", servlet.getName())
//                .keyValue("mapping", owner().getWebAppContext().getContextPath())
                ;
        this.servletName = servletName;
        this.servlet = servlet;
//        this.servletHolder = owner().getWebAppContext().addServlet(this.servlet, owner().getWebAppContext().getContextPath());
//        servletHolder.setDisplayName(this.servletName);
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

    @Override
    public Servlet<TomcatWebApp> addMapping(String mapping) {
        //owner().infoBuilder.keyValue("mapping", mapping);
        // do nothing - already set in servletHolder
        return this;
    }

    @Override
    public Servlet<TomcatWebApp> addInitParam(String name, String value) {
        owner().infoBuilder.key("initParam")
                .keyValue("name", name)
                .keyValue("value", value)
                .up();
//        servletHolder.setInitParameter(name, value);
        return this;
    }

    @Override
    public Servlet<TomcatWebApp> setAsyncSupported(boolean asyncSupported) {
        owner().infoBuilder.keyValue("asyncSupported", String.valueOf(asyncSupported));
//        servletHolder.setAsyncSupported(asyncSupported);
        return this;
    }

    @Override
    public Servlet<TomcatWebApp> setLoadOnStartup(int loadOnStartup) {
        owner().infoBuilder.keyValue("loadOnStartup", String.valueOf(loadOnStartup));
//        servletHolder.setInitOrder(loadOnStartup);
        return this;
    }

    @Override
    public Servlet<TomcatWebApp> setEnabled(boolean enabled) {
        owner().infoBuilder.keyValue("enabled", String.valueOf(enabled));
//        servletHolder.setEnabled(enabled);
        return this;
    }
}
