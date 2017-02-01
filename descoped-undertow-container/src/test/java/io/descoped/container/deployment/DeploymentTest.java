package io.descoped.container.deployment;

import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.undertow.core.UndertowContainer;
import io.descoped.container.undertow.deployment.RestDeployment;
import io.descoped.container.undertow.deployment.UndertowWebApp;
import org.apache.deltaspike.cdise.servlet.CdiServletRequestListener;
import org.apache.deltaspike.servlet.impl.event.EventBridgeFilter;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by oranheim on 23/01/2017.
 */
public class DeploymentTest {

    private static final Logger log = LoggerFactory.getLogger(DeploymentTest.class);

    @Test
    public void testFluentAPI() throws Exception {
        UndertowWebApp webapp = WebApp.create(UndertowWebApp.class)
                .name("dummy")
                .contextPath("/")
                .addListener(CdiServletRequestListener.class)
                .addServlet("jerseyServletContainer", ServletContainer.class)
                    .addMapping("/rest/*")
                    .setAsyncSupported(true)
                    .setEnabled(true)
                    .up()
                .addFilter("foo", EventBridgeFilter.class).up()
                ;

        log.info("deployment: \n{}", webapp.info());
        assertNotNull(webapp);
    }

    @Test
    public void testRestDeployment() throws Exception {
        UndertowContainer server = mock(UndertowContainer.class);
        when(server.getContextPath()).thenReturn("/ctx");
        UndertowWebApp webapp = RestDeployment.restDeployment(server, "/rest/*");
        log.info("rest deployment: \n{}", webapp.info());
    }

}
