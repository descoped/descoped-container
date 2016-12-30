package io.descoped.container.commands.geolocation;

import io.descoped.container.support.ConsoleAppender;
import io.descoped.container.support.Descoped;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

/**
 * Created by oranheim on 30/12/2016.
 */
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class CommandGetGeoLocationTest {

    private static final Logger log = LoggerFactory.getLogger(CommandGetGeoLocationTest.class);
    
    @Inject
    @Descoped
    Instance<GeoLocation> geoLocationInstance;

    @Produces
    @RequestScoped
    @Descoped
    private GeoLocation produceGeoLocation() {
        return new CommandGetGeoLocation().execute();
    }

    @Test
    public void testGetGeoLocation() {
        GeoLocation geoLocation = geoLocationInstance.get();
        assertNotNull(geoLocation);
        log.trace("GeoLocation: {}", geoLocation.toString());
    }

}
