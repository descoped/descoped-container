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
    protected static ThreadLocal<Integer> servletCount = ThreadLocal.withInitial(() -> 0);

    public JettyServlet(WebApp<JettyWebApp> owner, String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        this.owner = owner;
        owner().infoBuilder.key("addServlet" + servletCount.get())
                .keyValue("name", servletName)
                .keyValue("servlet", servlet.getName())
                .keyValue("mapping", owner().getWebAppContext().getContextPath());
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
        owner().infoBuilder.up();
        servletCount.set(servletCount.get()+1);
        return owner();
    }

    @Override
    public Servlet<JettyWebApp> addMapping(String mapping) {
        //owner().infoBuilder.keyValue("mapping", mapping);
        // do nothing - already set in servletHolder
        return this;
    }

    @Override
    public Servlet<JettyWebApp> addInitParam(String name, String value) {
        owner().infoBuilder.key("initParam")
                .keyValue("name", name)
                .keyValue("value", value)
                .up();
        servletHolder.setInitParameter(name, value);
        return this;
    }

    @Override
    public Servlet<JettyWebApp> setAsyncSupported(boolean asyncSupported) {
        owner().infoBuilder.keyValue("asyncSupported", String.valueOf(asyncSupported));
        servletHolder.setAsyncSupported(asyncSupported);
        return this;
    }

    @Override
    public Servlet<JettyWebApp> setLoadOnStartup(int loadOnStartup) {
        owner().infoBuilder.keyValue("loadOnStartup", String.valueOf(loadOnStartup));
        servletHolder.setInitOrder(loadOnStartup);
        return this;
    }

    @Override
    public Servlet<JettyWebApp> setEnabled(boolean enabled) {
        owner().infoBuilder.keyValue("enabled", String.valueOf(enabled));
        servletHolder.setEnabled(enabled);
        return this;
    }
}
