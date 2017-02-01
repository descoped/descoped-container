package io.descoped.container.core;

import com.jayway.restassured.http.ContentType;
import io.descoped.container.log.ConsoleAppender;
import io.descoped.container.support.WebServer;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.net.HttpURLConnection;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertNotEquals;

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

    @Inject
    @WebServer(id = "other")
    Instance<ServerContainer> otherContainerInstance;

    @Test
    public void testContainerInstances() throws Exception {
        assertNotEquals(defaultContainer, otherContainerInstance);
    }

    @Test
    public void testDefaultContainer() throws Exception {
        defaultContainer.start();
        given()
                .port(defaultContainer.getPort())
                .contentType(ContentType.XML.withCharset("UTF-8"))
                    .log().everything()
                .expect()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .log().everything()
                .when()
                    .get("/rest/test/")
        ;
        defaultContainer.shutdown();
    }

    @Test
    public void testOtherContainer() throws Exception {
        ServerContainer otherContainer = otherContainerInstance.get();
        otherContainer.start();
        given()
                .port(otherContainer.getPort())
                .contentType(ContentType.XML.withCharset("UTF-8"))
                    .log().everything()
                .expect()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .log().everything()
                .when()
                    .get("/rest/test/")
        ;
        otherContainer.shutdown();
    }

}
