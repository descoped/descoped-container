package io.descoped.container.undertow.deployment;

import io.descoped.container.deployment.spi.Servlet;
import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.undertow.core.CDIClassIntrospecter;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by oranheim on 23/01/2017.
 */
public class UndertowServlet implements Servlet<UndertowWebApp> {

    private static final Logger log = LoggerFactory.getLogger(UndertowServlet.class);
    private final WebApp<UndertowWebApp> owner;
    private final ServletInfo servletInfo;
    protected static ThreadLocal<Integer> servletCount = ThreadLocal.withInitial(() -> 0);

    public UndertowServlet(WebApp<UndertowWebApp> owner, String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        this.owner = owner;
        owner().infoBuilder.key("addServlet" + servletCount.get())
                .keyValue("name", servletName)
                .keyValue("servlet", servlet.getName());
        this.servletInfo = Servlets.servlet(servletName, servlet);
        try {
            this.servletInfo.setInstanceFactory(CDIClassIntrospecter.INSTANCE.createInstanceFactory(servlet));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            // do nothing
        }
    }

    private UndertowWebApp owner() {
        return (UndertowWebApp) owner;
    }

    private DeploymentInfo deploymentInfo() {
        return owner().getDeploymentInfo();
    }

    @Override
    public UndertowWebApp up() {
        owner().infoBuilder.up();
        servletCount.set(servletCount.get()+1);
        deploymentInfo().addServlet(servletInfo);
        return owner();
    }

    @Override
    public Servlet<UndertowWebApp> addMapping(String mapping) {
        owner().infoBuilder.keyValue("mapping", mapping);
        servletInfo.addMapping(mapping);
        return this;
    }

    @Override
    public Servlet<UndertowWebApp> addInitParam(String name, String value) {
        owner().infoBuilder.key("initParam")
                .keyValue("name", name)
                .keyValue("value", value)
                .up();
        servletInfo.addInitParam(name, value);
        return this;
    }

    @Override
    public Servlet<UndertowWebApp> setAsyncSupported(boolean asyncSupported) {
        owner().infoBuilder.keyValue("asyncSupported", String.valueOf(asyncSupported));
        servletInfo.setAsyncSupported(asyncSupported);
        return this;
    }

    @Override
    public Servlet<UndertowWebApp> setLoadOnStartup(int loadOnStartup) {
        owner().infoBuilder.keyValue("loadOnStartup", String.valueOf(loadOnStartup));
        servletInfo.setLoadOnStartup(loadOnStartup);
        return this;
    }

    @Override
    public Servlet<UndertowWebApp> setEnabled(boolean enabled) {
        owner().infoBuilder.keyValue("enabled", String.valueOf(enabled));
        servletInfo.setEnabled(enabled);
        return this;
    }
}
