package io.descoped.container.jetty.test;

import com.jayway.restassured.http.ContentType;
import io.descoped.container.core.ServerContainer;
import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.jetty.deployment.JettyDeployment;
import io.descoped.container.jetty.deployment.JettyWebApp;
import io.descoped.container.log.ConsoleAppender;
import io.descoped.container.support.WebServer;
import org.apache.deltaspike.cdise.servlet.CdiServletRequestListener;
import org.apache.deltaspike.servlet.impl.event.EventBridgeFilter;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import java.net.HttpURLConnection;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by oranheim on 22/12/2016.
 */
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class ContainerTest {

    private static final Logger log = LoggerFactory.getLogger(ContainerTest.class);

    @Inject
    @WebServer
    ServerContainer defaultContainer;

    @Before
    public void setUp() throws Exception {
        JettyDeployment deployment = new JettyDeployment();
        JettyWebApp webapp = WebApp.create(JettyWebApp.class)
                .name("dummy")
                .contextPath("/")
                .addListener(CdiServletRequestListener.class)
                .addServlet("dummyServlet", DummyServlet.class)
                    .addMapping("/*")
                    .up()
                .addFilter("EventBridgeFilter", EventBridgeFilter.class)
                    .addFilterUrlMapping("/*", DispatcherType.REQUEST)
                    .up()
                ;
        log.trace("webAppContext: {}", webapp.getWebAppContext().dump());
        deployment.deploy(webapp);
        defaultContainer.deploy(deployment);
        defaultContainer.start();
    }

    @After
    public void tearDown() throws Exception {
        defaultContainer.shutdown();
    }

//    @Test
    public void testMe() throws Exception {

    }

    @Test
    public void testDefaultContainer() throws Exception {
        given()
                .port(defaultContainer.getPort())
                .contentType(ContentType.HTML.withCharset("UTF-8"))
                    .log().everything()
                .expect()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .log().everything()
                .when()
                    .get("/test/")
        ;
    }

}
