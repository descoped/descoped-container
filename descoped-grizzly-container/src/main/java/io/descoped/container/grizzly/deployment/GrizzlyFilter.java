package io.descoped.container.grizzly.deployment;

import io.descoped.container.deployment.spi.Filter;
import io.descoped.container.deployment.spi.WebApp;

import javax.servlet.DispatcherType;

/**
 * Created by oranheim on 03/02/2017.
 */
public class GrizzlyFilter implements Filter<GrizzlyWebApp> {

    private final WebApp<GrizzlyWebApp> owner;
    private final String filterName;
    private final Class<? extends javax.servlet.Filter> filter;
    protected static ThreadLocal<Integer> filterCount = ThreadLocal.withInitial(() -> 0);

    public GrizzlyFilter(WebApp<GrizzlyWebApp> owner, String filterName, Class<? extends javax.servlet.Filter> filter) {
        this.owner = owner;
        owner().infoBuilder.key("addFilter" + filterCount.get())
                .keyValue("name", filterName)
                .keyValue("filter", filter.getName());
        this.filterName = filterName;
        this.filter = filter;
    }

    private GrizzlyWebApp owner() {
        return (GrizzlyWebApp) owner;
    }

    @Override
    public GrizzlyWebApp up() {
        owner().infoBuilder.up();
        filterCount.set(filterCount.get()+1);
        return owner();
    }

    // example: http://stackoverflow.com/questions/11645516/giving-multiple-url-patterns-to-servlet-filter
    @Override
    public Filter<GrizzlyWebApp> addFilterUrlMapping(String mapping, DispatcherType dispatcher) {
        owner().infoBuilder.key("urlMapping")
                .keyValue("name", filterName)
                .keyValue("mapping", mapping)
                .keyValue("dispatcher", String.valueOf(dispatcher))
                .up();
//        FilterHolder filterHolder = new FilterHolder(filter);
//        filterHolder.setName(filterName);
//        filterHolder.setInitParameter(filterName, mapping);
//        owner().getWebAppContext().addFilter(filterHolder, mapping, EnumSet.of(dispatcher));
        return this;
    }

    @Override
    public Filter<GrizzlyWebApp> addFilterServletNameMapping(String mapping, DispatcherType dispatcher) {
        owner().infoBuilder.key("servletNameMapping")
                .keyValue("name", filterName)
                .keyValue("mapping", mapping)
                .keyValue("dispatcher", String.valueOf(dispatcher))
                .up();
//        FilterHolder filterHolder = owner().getWebAppContext().addFilter(this.filter, mapping, EnumSet.of(dispatcher));
//        filterHolder.setName(filterName);
        return this;
    }
}
