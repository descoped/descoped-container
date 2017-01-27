package io.descoped.container.core;

import io.descoped.container.support.ConsoleAppender;
import io.descoped.container.support.WebServer;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Created by oranheim on 28/12/2016.
 */
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class BasicContainerLifecyleTest {

    private static final Logger log = LoggerFactory.getLogger(BasicContainerLifecyleTest.class);

    private static int count_lifecycle_start = 0;
    private static int count_lifecycle_stop = 0;
    private static int count_preStart_event = 0;
    private static int count_preStop_event = 0;

    @Inject
    @WebServer(id = "basic")
    ServerContainer container;

    public void onStartup(@Observes PreStartContainer event) {
        // deployment is handled by the common DeploymentHandler
        count_preStart_event++;
    }

    public void onShutdown(@Observes PreStopContainer event) {
        // do something - please note that undeployment is handled by each Deployment
        count_preStop_event++;
    }

    @AfterClass
    public static void done() throws Exception {
        assertEquals(2, count_lifecycle_start);
        assertEquals(2, count_lifecycle_stop);
//        assertEquals(2, count_preStart_event);
//        assertEquals(2, count_preStop_event);
    }

    @Before
    public void setUp() throws Exception {
        container.start();
        count_lifecycle_start++;
    }

    @After
    public void tearDown() throws Exception {
        container.shutdown();
        count_lifecycle_stop++;
    }

    @Test
    public void first_run() throws Exception {
        // trigger a start/stop core and events
    }

    @Test
    public void second_run() throws Exception {
        // trigger a start/stop core and events
    }

}
