package io.descoped.container.cdi;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.LogManager;

/**
 * Created by oranheim on 07/02/2017.
 */
//@RunWith(CdiTestRunner.class)
public class CdiTest {

    private static final Logger log = LoggerFactory.getLogger(CdiTest.class);
    private static CdiContainer cdiContainer;

    @BeforeClass
    public static void before() throws Exception {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        //LogManager.getLogManager().getLogger("").setLevel(Level.INFO);

        cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();
        cdiContainer.getContextControl().startContext(ApplicationScoped.class);
    }

    @AfterClass
    public static void after() throws Exception {
        cdiContainer.shutdown();
        cdiContainer = null;
    }

    @Test
    public void testMe() throws Exception {
        log.trace("I am here");

    }
}
