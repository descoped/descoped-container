package io.descoped.server;

import com.jayway.restassured.http.ContentType;
import io.descoped.server.container.Dep;
import io.descoped.server.container.DeploymentEvent;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.lang.annotation.Annotation;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.LogManager;

import static com.jayway.restassured.RestAssured.given;

@RunWith(CdiTestRunner.class)
public class MainTest {

    private static final Logger log = LoggerFactory.getLogger(MainTest.class);
    private Main server;

    @BeforeClass
    public static void before() throws Exception {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        //LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
    }

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
    public void testMe() throws Exception {
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

    //@Test
    public void testMe2() throws Exception {
        Class<?> clazz = Dep.class;
        List<?> foo = BeanProvider.getContextualReferences(clazz, true, true);
        log.info("foo={}", foo.size());
        foo.forEach(i -> {
            log.trace("item: {}", ((Dep)i).val());
        });
    }

    @Test
    public void testMe3() throws Exception {
        List<Dep> foo = BeanProvider.getContextualReferences(Dep.class, false);
        log.info("foo={}", foo.size());
        foo.forEach(i -> {
            log.trace("item: {}", ((Dep)i).val());
        });
    }

}
