package io.descoped.container.support.jersey;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.ext.cdi1x.internal.LocalizationMessages;
import org.glassfish.jersey.ext.cdi1x.internal.spi.Hk2LocatorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;

/**
 * Created by oranheim on 29/12/2016.
 */
public class DescopedHk2LocatorManager implements Hk2LocatorManager {

    private static final Logger log = LoggerFactory.getLogger(DescopedHk2LocatorManager.class);

    private volatile ServiceLocator locator;

    @Override
    public void registerLocator(ServiceLocator locator) {
        log.trace("Register HK2 CDI Locator Manager: {}", locator);
        if (this.locator == null || locator != null) {
            this.locator = locator;
        } else if (this.locator != locator) {
            throw new WebApplicationException(LocalizationMessages.CDI_MULTIPLE_LOCATORS_INTO_SIMPLE_APP());
        }
    }

    @Override
    public ServiceLocator getEffectiveLocator() {
        return locator;
    }
}
