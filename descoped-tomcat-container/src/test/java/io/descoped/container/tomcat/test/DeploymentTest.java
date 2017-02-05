package io.descoped.container.tomcat.test;

import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.tomcat.core.TomcatContainer;
import io.descoped.container.tomcat.deployment.TomcatDeployment;
import io.descoped.container.tomcat.deployment.TomcatWebApp;
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
public class DeploymentTest {

    private static final Logger log = LoggerFactory.getLogger(DeploymentTest.class);

    @Test
    public void testFluentAPI() throws Exception {
        TomcatWebApp webapp = WebApp.create(TomcatWebApp.class)
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
        TomcatContainer server = mock(TomcatContainer.class);
        when(server.getContextPath()).thenReturn("/ctx");
        TomcatDeployment deployment = new TomcatDeployment();
        server.deploy(deployment);
//        log.info("rest deployment: \n{}", deployment.info());
    }

}
