package io.descoped.server;

import io.descoped.server.container.ServerContainer;
import io.descoped.server.deployment.RestDeployment;
import io.descoped.server.support.ConsoleAppender;
import io.descoped.server.support.WebServer;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by oranheim on 28/12/2016.
 */
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class CLTest {

    private static final Logger log = LoggerFactory.getLogger(CLTest.class);

    @Inject
    @WebServer(id = "clTest")
    ServerContainer container;

    @Before
    public void setUp() throws Exception {
        try {
            container.deploy(new RestDeployment());
            container.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.trace("---------> Server [{}]: {}", this, container);
    }

    @After
    public void tearDown() throws Exception {
        container.shutdown();
//        BeanManager bm = BeanManagerProvider.getInstance().getBeanManager();
//        bm.getExtension(CdiComponentProvider.class).


    }

    @Test
    public void first_run() throws Exception {
    }

    @Test
    public void second_run() throws Exception {

    }

}
