package io.descoped.container.core;

import io.descoped.container.Main;
import io.descoped.container.log.ConsoleAppender;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class MainTest {

    private static final Logger log = LoggerFactory.getLogger(MainTest.class);
    private Main main;

    @Before
    public void setUp() throws Exception {
        main = new Main();
        log.info("Start main..: {}", main.isRunning());
        main.start();
        log.info("Started main!");
        assertEquals(3, main.serviceCount());
    }

    @After
    public void tearDown() throws Exception {
        main.stop();
        log.info("Shutdown main!");
        assertEquals(0, main.serviceCount());
    }

    @Test
    public void testDiscovery() throws Exception {

    }

//    @Test
//    public void testRestTest() throws Exception {
//        given()
////            .port(main.getPort())
//            .contentType(ContentType.XML.withCharset("UTF-8"))
//                .log().everything()
//            .expect()
//                .statusCode(HttpURLConnection.HTTP_OK)
//                .log().everything()
//            .when()
//                .get("/rest/test/")
//            ;
//    }


}
