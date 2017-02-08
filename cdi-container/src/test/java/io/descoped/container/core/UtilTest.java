package io.descoped.container.core;

import io.descoped.container.cdi.log.ConsoleAppender;
import io.descoped.container.cdi.log.Log;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Created by oranheim on 10/12/2016.
 */
//@Ignore
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class UtilTest {

    @Inject
    @Log
    Logger log;

    @Inject
    @ConfigProperty(name = "foo")
    String foo;

    @Test
    public void testConfig() throws Exception {
        String foo = ConfigResolver.getPropertyValue("foo");
        log.trace("foo={}", foo);
        assertEquals(this.foo, foo);
    }

}
