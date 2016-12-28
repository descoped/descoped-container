package io.descoped.server;

import com.jayway.restassured.http.ContentType;
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
public class ServerExtTest {

    private static final Logger log = LoggerFactory.getLogger(ServerExtTest.class);

    @Inject
    @WebServer(id = "first")
    ServerContainer server;

    @Inject
    @WebServer(id = "other")
    Instance<ServerContainer> server2;

    @Before
    public void setUp() throws Exception {
//        server.deploy(new RestDeployment());
//        server.start();
        log.trace("---------> Server [{}]: {}", this, server);
    }

    @After
    public void tearDown() throws Exception {
//        server.shutdown();
    }

    @Test
    public void testServer() throws Exception {
        log.trace("----------------------> PORT: {}", server.getPort());
        server.deploy(new RestDeployment());
        server.start();
        log.trace("------> server: {}={} {}", server.hashCode(), server2.hashCode(), (server.hashCode() == server2.hashCode()));
        log.trace("------> server2: {}", server2);
        assertNotEquals(server2, server);

        given()
                .port(server.getPort())
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
//        ServerContainer server2Instance = server2.get();
        ServerContainer server2Instance = server;
        server2Instance.deploy(new RestDeployment());
        server2Instance.start();
        log.trace("---------> Server [{}]: {}", this, server);
        given()
                .port(server2Instance.getPort())
                .contentType(ContentType.XML.withCharset("UTF-8"))
                    .log().everything()
                .expect()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .log().everything()
                .when()
                    .get("/test/")
        ;
        server2Instance.shutdown();
    }

}
