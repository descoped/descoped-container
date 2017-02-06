package io.descoped.support.jndi.test;

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

/**
 * Created by oranheim on 06/02/2017.
 */
@RunWith(JUnit4.class)
public class JndiFactoryTest {

    private static final Logger log = LoggerFactory.getLogger(JndiFactoryTest.class);
    private DescopedContainer container;

    @Before
    public void setUp() throws Exception {
        InstanceFactory<DescopedPrimitive> factory = DefaultInstanceFactory.get(SpiInstanceFactory.class);
        container = new DescopedContainer<>(factory);
        container.start();
    }

    @After
    public void tearDown() throws Exception {
        container.stop();
        container = null;
    }

    @Test
    public void testJndiFactory() throws Exception {
        log.trace("Hello");
    }

}
