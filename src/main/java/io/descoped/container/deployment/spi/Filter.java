package io.descoped.container.deployment.spi;

import javax.servlet.DispatcherType;

/**
 * Created by oranheim on 23/01/2017.
 */
public interface Filter<F extends WebApp> {

    F up();

    Filter<F> addFilterUrlMapping(final String filterName, final String mapping, DispatcherType dispatcher);

    Filter<F> addFilterServletNameMapping(final String filterName, final String mapping, DispatcherType dispatcher);

}
