package io.descoped.container.weld.test;

import io.descoped.container.weld.cdictrl.DescopedWeld;
import io.descoped.logger.logback.handler.LogbackBridgeHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.CDI;

import static org.junit.Assert.assertTrue;

/**
 * Created by oranheim on 06/02/2017.
 */
@RunWith(JUnit4.class)
public class DescopedWeldTest {

    private static final Logger log = LoggerFactory.getLogger(DescopedWeldTest.class);
    private static DescopedWeld weld;

    @BeforeClass
    public static void before() throws Exception {
        LogbackBridgeHandler.installJavaUtilLoggerBridgeHandler();
        weld = new DescopedWeld();
        log.trace("Created: {}", weld.getClass());
        weld.initialize();
    }

    @AfterClass
    public static void after() throws Exception {
        weld.shutdown();
        weld = null;
    }

    @Test
    public void testDescopedWeld() throws Exception {
        Context ctx = CDI.current().getBeanManager().getContext(ApplicationScoped.class);
        assertTrue(ctx.isActive());
    }
}
