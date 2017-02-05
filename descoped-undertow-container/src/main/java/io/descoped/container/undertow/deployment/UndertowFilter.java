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
    protected static ThreadLocal<Integer> filterCount = ThreadLocal.withInitial(() -> 0);

    public UndertowFilter(WebApp<UndertowWebApp> owner, String filterName, Class<? extends javax.servlet.Filter> filter) {
        this.owner = owner;
        owner().infoBuilder.key("addFilter" + filterCount.get())
                .keyValue("name", filterName)
                .keyValue("filter", filter.getName());
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
        owner().infoBuilder.up();
        filterCount.set(filterCount.get()+1);
        deploymentInfo().addFilter(filterInfo);
        return owner();
    }

    @Override
    public Filter<UndertowWebApp> addFilterUrlMapping(String mapping, DispatcherType dispatcher) {
        owner().infoBuilder.key("urlMapping")
                .keyValue("name", filterInfo.getName())
                .keyValue("mapping", mapping)
                .keyValue("dispatcher", String.valueOf(dispatcher))
                .up();
        deploymentInfo().addFilterUrlMapping(filterInfo.getName(), mapping, dispatcher);
        return this;
    }

    @Override
    public Filter<UndertowWebApp> addFilterServletNameMapping(String mapping, DispatcherType dispatcher) {
        owner().infoBuilder.key("servletNameMapping")
                .keyValue("name", filterInfo.getName())
                .keyValue("mapping", mapping)
                .keyValue("dispatcher", String.valueOf(dispatcher))
                .up();
        deploymentInfo().addFilterServletNameMapping(filterInfo.getName(), mapping, dispatcher);
        return this;
    }

}
