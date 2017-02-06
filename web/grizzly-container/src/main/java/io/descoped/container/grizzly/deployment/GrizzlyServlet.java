package io.descoped.container.grizzly.deployment;

import io.descoped.container.deployment.spi.Servlet;
import io.descoped.container.deployment.spi.WebApp;
import org.glassfish.grizzly.servlet.ServletRegistration;

/**
 * Created by oranheim on 03/02/2017.
 */
public class GrizzlyServlet implements Servlet<GrizzlyWebApp> {

    private final WebApp<GrizzlyWebApp> owner;
    private final String servletName;
    private final Class<? extends javax.servlet.Servlet> servlet;
    protected static ThreadLocal<Integer> servletCount = ThreadLocal.withInitial(() -> 0);
    private final ServletRegistration servletHolder;

    public GrizzlyServlet(WebApp<GrizzlyWebApp> owner, String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        this.owner = owner;
        this.servletName = servletName;
        this.servlet = servlet;

        owner().infoBuilder.key("addServlet" + servletCount.get())
                .keyValue("name", servletName)
                .keyValue("servlet", servlet.getName())
                ;
        servletHolder = owner().getWebAppContext().addServlet(servletName, servlet);
    }

    private GrizzlyWebApp owner() {
        return (GrizzlyWebApp) owner;
    }

    @Override
    public GrizzlyWebApp up() {
        owner().infoBuilder.up();
        servletCount.set(servletCount.get()+1);
        return owner();
    }

    @Override
    public Servlet<GrizzlyWebApp> addMapping(String mapping) {
        owner().infoBuilder.keyValue("mapping", mapping);
        servletHolder.addMapping(mapping);
        return this;
    }

    @Override
    public Servlet<GrizzlyWebApp> addInitParam(String name, String value) {
        owner().infoBuilder.key("initParam")
                .keyValue("name", name)
                .keyValue("value", value)
                .up();
        servletHolder.setInitParameter(name, value);
        return this;
    }

    @Override
    public Servlet<GrizzlyWebApp> setAsyncSupported(boolean asyncSupported) {
        owner().infoBuilder.keyValue("asyncSupported", String.valueOf(asyncSupported));
        servletHolder.setAsyncSupported(asyncSupported);
        return this;
    }

    @Override
    public Servlet<GrizzlyWebApp> setLoadOnStartup(int loadOnStartup) {
        owner().infoBuilder.keyValue("loadOnStartup", String.valueOf(loadOnStartup));
        servletHolder.setLoadOnStartup(loadOnStartup);
        return this;
    }

    @Override
    public Servlet<GrizzlyWebApp> setEnabled(boolean enabled) {
//        owner().infoBuilder.keyValue("enabled", String.valueOf(enabled));
//        servletHolder.setEnabled(enabled);
        // do nothing - grizzly doesn't support enabled
        return this;
    }
}
