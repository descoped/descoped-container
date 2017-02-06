package io.descoped.container.jetty.test;

import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.jetty.core.JettyContainer;
import io.descoped.container.jetty.deployment.JettyDeployment;
import io.descoped.container.jetty.deployment.JettyWebApp;
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
public class JettyDeploymentTest {

    private static final Logger log = LoggerFactory.getLogger(JettyDeploymentTest.class);

    @Test
    public void testFluentAPI() throws Exception {
        JettyWebApp webapp = WebApp.create(JettyWebApp.class)
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
        JettyContainer server = mock(JettyContainer.class);
        when(server.getContextPath()).thenReturn("/ctx");
        JettyDeployment deployment = new JettyDeployment();
        server.deploy(deployment);
//        log.info("rest deployment: \n{}", deployment.info());
    }

}
