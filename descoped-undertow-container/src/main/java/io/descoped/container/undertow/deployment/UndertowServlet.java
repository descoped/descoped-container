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
    private StringBuilder infoBuilder = new StringBuilder();
    protected static ThreadLocal<Integer> servletCount = ThreadLocal.withInitial(() -> 0);

    public UndertowServlet(WebApp<UndertowWebApp> owner, String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        this.owner = owner;
        infoBuilder.append("\t\t\"").append("name").append("\"").append(": \"").append(servletName).append("\"");
        infoBuilder.append(",\n\t\t\"").append("servlet").append("\"").append(": \"").append(servlet.getName()).append("\"");
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
        owner().addInfo("addServlet" + servletCount.get(), infoBuilder); servletCount.set(servletCount.get()+1);
        deploymentInfo().addServlet(servletInfo);
        return owner();
    }

    @Override
    public Servlet<UndertowWebApp> addMapping(String mapping) {
        infoBuilder.append(",\n\t\t\"").append("mapping").append("\"").append(": \"").append(mapping).append("\"");
        servletInfo.addMapping(mapping);
        return this;
    }

    @Override
    public Servlet<UndertowWebApp> addInitParam(String name, String value) {
        infoBuilder.append(",\n\t\t\"").append("initParam").append("\"").append(": {");
        infoBuilder.append("\n\t\t\t\"").append("name").append("\": \"").append(name).append("\",");
        infoBuilder.append("\n\t\t\t\"").append("value").append("\": \"").append(value).append("\"");
        infoBuilder.append("\n\t\t}");
        servletInfo.addInitParam(name, value);
        return this;
    }

    @Override
    public Servlet<UndertowWebApp> setAsyncSupported(boolean asyncSupported) {
        infoBuilder.append(",\n\t\t\"").append("asyncSupported").append("\"").append(": \"").append(asyncSupported).append("\"");
        servletInfo.setAsyncSupported(asyncSupported);
        return this;
    }

    @Override
    public Servlet<UndertowWebApp> setLoadOnStartup(int loadOnStartup) {
        infoBuilder.append(",\n\t\t\"").append("loadOnStartup").append("\"").append(": \"").append(loadOnStartup).append("\"");
        servletInfo.setLoadOnStartup(loadOnStartup);
        return this;
    }

    @Override
    public Servlet<UndertowWebApp> setEnabled(boolean enabled) {
        infoBuilder.append(",\n\t\t\"").append("enabled").append("\"").append(": \"").append(enabled).append("\"");
        servletInfo.setEnabled(enabled);
        return this;
    }
}
