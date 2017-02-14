package io.descoped.container.core;

import io.descoped.container.module.DescopedContainer;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.spi.SpiInstanceFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class DescopedSpiContainerTest {

    private static final Logger log = LoggerFactory.getLogger(DescopedSpiContainerTest.class);
    private DescopedContainer descopedContainer;

    @Before
    public void setUp() throws Exception {
        InstanceFactory<DescopedPrimitive> factory = DefaultInstanceFactory.get(SpiInstanceFactory.class);
        descopedContainer = new DescopedContainer(factory);
        log.info("Start descopedContainer..: {}", descopedContainer.isRunning());
        descopedContainer.start();
        log.info("Started descopedContainer!");
        assertEquals(1, descopedContainer.serviceCount());
    }

    @After
    public void tearDown() throws Exception {
        descopedContainer.stop();
        log.info("Shutdown descopedContainer!");
        assertEquals(0, descopedContainer.serviceCount());
    }

    @Test
    public void testDiscovery() throws Exception {
    }

}
