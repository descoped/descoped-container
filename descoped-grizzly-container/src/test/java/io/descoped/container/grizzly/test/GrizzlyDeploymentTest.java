package io.descoped.container.grizzly.test;

import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.grizzly.core.GrizzlyContainer;
import io.descoped.container.grizzly.deployment.GrizzlyDeployment;
import io.descoped.container.grizzly.deployment.GrizzlyWebApp;
import org.apache.deltaspike.cdise.servlet.CdiServletRequestListener;
import org.apache.deltaspike.servlet.impl.event.EventBridgeFilter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by oranheim on 23/01/2017.
 */
public class GrizzlyDeploymentTest {

    private static final Logger log = LoggerFactory.getLogger(GrizzlyDeploymentTest.class);

    @Test
    public void testFluentAPI() throws Exception {
        GrizzlyWebApp webapp = WebApp.create(GrizzlyWebApp.class)
                .name("dummy")
                .contextPath("/")
                .addListener(CdiServletRequestListener.class)
                .addServlet("dummyServlet", DummyServlet.class)
                    .addMapping("/*")
                    .up()
                .addFilter("dummyServletFilter", EventBridgeFilter.class)
                    .addFilterUrlMapping("/*", DispatcherType.REQUEST)
                    .up()
                ;

        log.info("deployment: \n{}", webapp.info());
        assertNotNull(webapp);
    }

    @Test
    public void testRestDeployment() throws Exception {
        GrizzlyContainer server = mock(GrizzlyContainer.class);
        when(server.getContextPath()).thenReturn("/ctx");
        GrizzlyDeployment deployment = new GrizzlyDeployment();
        server.deploy(deployment);
//        log.info("rest deployment: \n{}", deployment.info());
    }

}
