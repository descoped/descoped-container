package io.descoped.server;

import io.descoped.server.container.UndertowContainer;
import io.descoped.server.support.ConsoleAppender;
import io.descoped.server.support.WebServer;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by oranheim on 22/12/2016.
 */
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class ServerExtTest {

    private static final Logger log = LoggerFactory.getLogger(ServerExtTest.class);

    @Inject
    @WebServer
    UndertowContainer server;

    @Test
    public void testServer() throws Exception {
        log.trace("---------> Server [{}]: {}", this, server);
    }

}
