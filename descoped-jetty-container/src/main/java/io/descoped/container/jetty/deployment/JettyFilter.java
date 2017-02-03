package io.descoped.container.jetty.deployment;

import io.descoped.container.deployment.spi.Filter;
import io.descoped.container.deployment.spi.WebApp;
import org.eclipse.jetty.servlet.FilterHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Created by oranheim on 03/02/2017.
 */
public class JettyFilter implements Filter<JettyWebApp> {

    private final WebApp<JettyWebApp> owner;
    private final String filterName;
    private final Class<? extends javax.servlet.Filter> filter;
//    private final Filter<JettyWebApp> filterHolder;

    public JettyFilter(WebApp<JettyWebApp> owner, String filterName, Class<? extends javax.servlet.Filter> filter) {
        this.owner = owner;
        this.filterName = filterName;
        this.filter = filter;
    }

    private JettyWebApp owner() {
        return (JettyWebApp) owner;
    }

    @Override
    public JettyWebApp up() {
        return owner();
    }

    @Override
    public Filter<JettyWebApp> addFilterUrlMapping(String mapping, DispatcherType dispatcher) {
        FilterHolder filterHolder = owner().getWebAppContext().addFilter(this.filter, mapping, EnumSet.of(dispatcher));
        filterHolder.setName(filterName);
//        filterHolder.set
        return this;
    }

    @Override
    public Filter<JettyWebApp> addFilterServletNameMapping(String mapping, DispatcherType dispatcher) {
        FilterHolder filterHolder = owner().getWebAppContext().addFilter(this.filter, mapping, EnumSet.of(dispatcher));
        filterHolder.setName(filterName);
        return this;
    }
}
