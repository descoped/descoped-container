package io.descoped.container.undertow.deployment;

import io.descoped.container.deployment.spi.Filter;
import io.descoped.container.deployment.spi.WebApp;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.FilterInfo;

import javax.servlet.DispatcherType;

/**
 * Created by oranheim on 23/01/2017.
 */
public class UndertowFilter implements Filter<UndertowWebApp> {

    private final WebApp<UndertowWebApp> owner;
    private final FilterInfo filterInfo;
    private StringBuilder infoBuilder = new StringBuilder();
    protected static ThreadLocal<Integer> filterCount = ThreadLocal.withInitial(() -> 0);

    public UndertowFilter(WebApp<UndertowWebApp> owner, String filterName, Class<? extends javax.servlet.Filter> filter) {
        this.owner = owner;
        infoBuilder.append("\t\t\"").append("name").append("\"").append(": \"").append(filterName).append("\"");
        infoBuilder.append(",\n\t\t\"").append("filter").append("\"").append(": \"").append(filter.getName()).append("\"");
        this.filterInfo = Servlets.filter(filterName, filter);
    }

    private UndertowWebApp owner() {
        return (UndertowWebApp) owner;
    }

    private DeploymentInfo deploymentInfo() {
        return owner().getDeploymentInfo();
    }

    @Override
    public UndertowWebApp up() {
        owner().addInfo("addFilter" + filterCount.get(), infoBuilder); filterCount.set(filterCount.get()+1);
        deploymentInfo().addFilter(filterInfo);
        return owner();
    }

    @Override
    public Filter<UndertowWebApp> addFilterUrlMapping(String mapping, DispatcherType dispatcher) {
        infoBuilder.append(",\n\t\t\"").append("urlMapping").append("\"").append(": {");
        infoBuilder.append("\n\t\t\t\"").append("name").append("\": \"").append(filterInfo.getName()).append("\",");
        infoBuilder.append("\n\t\t\t\"").append("mapping").append("\": \"").append(mapping).append("\",");
        infoBuilder.append("\n\t\t\t\"").append("dispatcher").append("\": \"").append(dispatcher).append("\"");
        infoBuilder.append("\n\t\t}");
        deploymentInfo().addFilterUrlMapping(filterInfo.getName(), mapping, dispatcher);
        return this;
    }

    @Override
    public Filter<UndertowWebApp> addFilterServletNameMapping(String mapping, DispatcherType dispatcher) {
        infoBuilder.append(",\n\t\t\"").append("servletNameMapping").append("\"").append(": {");
        infoBuilder.append("\n\t\t\t\"").append("name").append("\": \"").append(filterInfo.getName()).append("\",");
        infoBuilder.append("\n\t\t\t\"").append("mapping").append("\": \"").append(mapping).append("\",");
        infoBuilder.append("\n\t\t\t\"").append("dispatcher").append("\": \"").append(dispatcher).append("\"");
        deploymentInfo().addFilterServletNameMapping(filterInfo.getName(), mapping, dispatcher);
        return this;
    }

}
