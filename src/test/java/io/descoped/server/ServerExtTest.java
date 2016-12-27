package io.descoped.server;

import com.jayway.restassured.http.ContentType;
import io.descoped.server.container.ServerContainer;
import io.descoped.server.deployment.RestDeployment;
import io.descoped.server.support.ConsoleAppender;
import io.descoped.server.support.WebServer;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.HttpURLConnection;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by oranheim on 22/12/2016.
 */
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class ServerExtTest {

    private static final Logger log = LoggerFactory.getLogger(ServerExtTest.class);

    @Inject
    @WebServer
    ServerContainer server;

    @Test
    public void testServer() throws Exception {
        server.start();
        server.deploy(new RestDeployment());
        log.trace("---------> Server [{}]: {}", this, server);
        given()
                .contentType(ContentType.XML.withCharset("UTF-8"))
                    .log().everything()
                .expect()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .log().everything()
                .when()
                    .get("/test/")
        ;

        server.shutdown();
    }

    @Test
    public void testServer2() throws Exception {
        server.start();
        server.deploy(new RestDeployment());
        log.trace("---------> Server [{}]: {}", this, server);
        given()
                .contentType(ContentType.XML.withCharset("UTF-8"))
                    .log().everything()
                .expect()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .log().everything()
                .when()
                    .get("/test/")
        ;
        server.shutdown();
    }

}
