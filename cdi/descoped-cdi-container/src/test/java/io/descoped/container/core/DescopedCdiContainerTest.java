package io.descoped.container.core;

import io.descoped.container.cdi.factory.CdiInstanceFactory;
import io.descoped.container.cdi.log.ConsoleAppender;
import io.descoped.container.module.DescopedContainer;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Ignore
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class DescopedCdiContainerTest {

    private static final Logger log = LoggerFactory.getLogger(DescopedCdiContainerTest.class);
    private DescopedContainer descopedContainer;

    @Before
    public void setUp() throws Exception {
        InstanceFactory<DescopedPrimitive> factory = DefaultInstanceFactory.get(CdiInstanceFactory.class);
        descopedContainer = new DescopedContainer(factory);
        log.info("Start descopedContainer..: {}", descopedContainer.isRunning());
        descopedContainer.start();
        log.info("Started descopedContainer!");
        Assert.assertEquals(3, descopedContainer.serviceCount());
    }

    @After
    public void tearDown() throws Exception {
        descopedContainer.stop();
        log.info("Shutdown descopedContainer!");
        Assert.assertEquals(0, descopedContainer.serviceCount());
    }

    @Test
    public void testDiscovery() throws Exception {
        // do nothing
    }

}
