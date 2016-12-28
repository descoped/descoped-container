package org.glassfish.jersey.ext.cdi1x.internal;

import org.glassfish.hk2.api.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;

/**
 * Created by oranheim on 29/12/2016.
 */
public class DescopedCdiComponentProvider extends CdiComponentProvider {

    private static final Logger log = LoggerFactory.getLogger(DescopedCdiComponentProvider.class);

    public DescopedCdiComponentProvider() {
        super();
        log.trace("---------------------> Hello: {}", this);
    }

    @Override
    ServiceLocator getEffectiveLocator() {
        ServiceLocator locator = super.getEffectiveLocator();
        return locator;
    }

    @Override
    public void initialize(ServiceLocator locator) {
        try {
            log.trace("---------------------> INIT: {}", locator);
            super.initialize(locator);
        } catch (WebApplicationException e) {
            log.trace("---------------------> HELO: {}", e.toString());
        }
    }

    @Override
    void addLocator(ServiceLocator locator) {
        try {
            log.trace("---------------------> REG: {}", locator);
            super.addLocator(locator);
        } catch (WebApplicationException e) {
            log.trace("---------------------> Hello: {}", e);
        }
    }
}
