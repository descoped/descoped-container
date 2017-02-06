package io.descoped.container.tomcat.deployment;

import io.descoped.container.deployment.spi.Filter;
import io.descoped.container.deployment.spi.WebApp;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.DispatcherType;

/**
 * Created by oranheim on 03/02/2017.
 */
public class TomcatFilter implements Filter<TomcatWebApp> {

    private final WebApp<TomcatWebApp> owner;
    private final String filterName;
    private final Class<? extends javax.servlet.Filter> filter;
    protected static ThreadLocal<Integer> filterCount = ThreadLocal.withInitial(() -> 0);
    private final FilterDef filterDef;

    public TomcatFilter(WebApp<TomcatWebApp> owner, String filterName, Class<? extends javax.servlet.Filter> filter) {
        this.owner = owner;
        owner().infoBuilder.key("addFilter" + filterCount.get())
                .keyValue("name", filterName)
                .keyValue("filter", filter.getName());
        this.filterName = filterName;
        this.filter = filter;
        filterDef = new FilterDef();
        filterDef.setFilterName(filterName);
        try {
            filterDef.setFilter(filter.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
        }
        owner().getStandardContext().addFilterDef(filterDef);
    }

    private TomcatWebApp owner() {
        return (TomcatWebApp) owner;
    }

    @Override
    public TomcatWebApp up() {
        owner().infoBuilder.up();
        filterCount.set(filterCount.get()+1);
        return owner();
    }

    // example: http://stackoverflow.com/questions/11645516/giving-multiple-url-patterns-to-servlet-filter
    @Override
    public Filter<TomcatWebApp> addFilterUrlMapping(String mapping, DispatcherType dispatcher) {
        owner().infoBuilder.key("urlMapping")
                .keyValue("name", filterName)
                .keyValue("mapping", mapping)
                .keyValue("dispatcher", String.valueOf(dispatcher))
                .up();
        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName(filterName);
        filterMap.addURLPattern(mapping);
        filterMap.setDispatcher(dispatcher.name());
        owner().getStandardContext().addFilterMap(filterMap);
        return this;
    }

    @Override
    public Filter<TomcatWebApp> addFilterServletNameMapping(String mapping, DispatcherType dispatcher) {
        owner().infoBuilder.key("servletNameMapping")
                .keyValue("name", filterName)
                .keyValue("mapping", mapping)
                .keyValue("dispatcher", String.valueOf(dispatcher))
                .up();
        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName(filterName);
        filterMap.addServletName(filterName); // todo: this must be investigated
        filterMap.setDispatcher(dispatcher.name());
        owner().getStandardContext().addFilterMap(filterMap);
        return this;
    }
}
