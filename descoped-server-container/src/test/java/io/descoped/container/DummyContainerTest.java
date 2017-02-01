package io.descoped.container;

import io.descoped.container.core.ServerContainer;
import io.descoped.container.log.ConsoleAppender;
import io.descoped.container.support.WebServer;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

/**
 * Created by oranheim on 01/02/2017.
 */
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class DummyContainerTest {

    private static final Logger log = LoggerFactory.getLogger(DummyContainerTest.class);

    @Inject
    @WebServer(id = "basic")
    ServerContainer container;

    @Test
    public void testContainer() throws Exception {
        assertNotNull(container);
        log.trace("-------------> container: {}", container);
    }

}
