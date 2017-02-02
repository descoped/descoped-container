package io.descoped.container.jetty.deployment;

import io.descoped.container.deployment.spi.Filter;

import javax.servlet.DispatcherType;

/**
 * Created by oranheim on 03/02/2017.
 */
public class JettyFilter implements Filter<JettyWebApp> {
    @Override
    public JettyWebApp up() {
        return null;
    }

    @Override
    public Filter<JettyWebApp> addFilterUrlMapping(String mapping, DispatcherType dispatcher) {
        return null;
    }

    @Override
    public Filter<JettyWebApp> addFilterServletNameMapping(String mapping, DispatcherType dispatcher) {
        return null;
    }
}
