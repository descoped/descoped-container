package io.descoped.server;

import com.jayway.restassured.http.ContentType;
import io.descoped.server.support.ConsoleAppender;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;

import static com.jayway.restassured.RestAssured.given;

@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class MainTest {

    private static final Logger log = LoggerFactory.getLogger(MainTest.class);
    private Main server;

    @Before
    public void setUp() throws Exception {
        server = new Main();
        log.info("Start server..: {}", server.isRunning());
        server.start();
        log.info("Started server!");
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
        log.info("Shutdown server!");
    }

    @Test
    public void testRestTest() throws Exception {
        given()
            .contentType(ContentType.XML.withCharset("UTF-8"))
                .log().everything()
            .expect()
                .statusCode(HttpURLConnection.HTTP_OK)
                .log().everything()
            .when()
                .get("/test/")
            ;
    }

    @Test
    public void testX() throws Exception {

    }

}
