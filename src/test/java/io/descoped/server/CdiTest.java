package io.descoped.server;

import io.descoped.server.support.MyQualifier;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.inject.Inject;
import java.util.logging.LogManager;

import static org.junit.Assert.assertEquals;

/**
 * Created by oranheim on 10/12/2016.
 */
@RunWith(CdiTestRunner.class)
public class CdiTest {

    @BeforeClass
    public static void before() throws Exception {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        //LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
    }

    @Inject
    @ConfigProperty(name = "foo")
    String foo;

    @Inject
    Logger log;

    @Inject @MyQualifier
    private String hello;

    @Test
    public void testMe() throws Exception {
        log.debug(hello);
    }

    @Test
    public void testConfig() throws Exception {
        String foo = ConfigResolver.getPropertyValue("foo");
        log.trace("foo={}", foo);
        assertEquals(this.foo, foo);
    }


}
