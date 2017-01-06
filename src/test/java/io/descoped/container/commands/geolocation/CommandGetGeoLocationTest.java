package io.descoped.container.commands.geolocation;

import com.jayway.restassured.http.ContentType;
import io.descoped.container.core.ServerContainer;
import io.descoped.container.support.ConsoleAppender;
import io.descoped.container.support.Descoped;
import io.descoped.container.support.DescopedLiteral;
import io.descoped.container.support.WebServer;
import org.apache.deltaspike.core.api.literal.DeltaSpikeLiteral;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.net.HttpURLConnection;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;

/**
 * Created by oranheim on 30/12/2016.
 */
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class CommandGetGeoLocationTest {

    private static final Logger log = LoggerFactory.getLogger(CommandGetGeoLocationTest.class);

    /**
     * Test resource is used to resolve GeoLocation during an active http request/response
     */
    @Path("/geolocation")
    @RequestScoped
    public static class GeoLocationResource {
        @GET
        @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
        public String get() {
            GeoLocation location = CDI.current().select(GeoLocation.class, DescopedLiteral.INSTANCE).get();
            log.trace("Response: {}", location);
            assertNotNull(location.getIpAddress());
            return "{\"status\": \"ok\"}";
        }
    }

    @Inject
    @WebServer(id = "geolocation")
    ServerContainer geocontainer;

    @Produces
    @RequestScoped
    @Descoped
    private GeoLocation produceGeoLocation() {
        HttpServletRequest request = CDI.current().select(HttpServletRequest.class, DeltaSpikeLiteral.INSTANCE).get();
        String remoteHost = request.getRemoteHost();
        return new CommandGetGeoLocation(remoteHost).execute();
    }

    @Test
    public void testRemoteAddr() throws Exception {
        geocontainer.start();

        given()
                .port(geocontainer.getPort())
                .contentType(ContentType.XML.withCharset("UTF-8"))
                    .log().everything()
                .expect()
                    .statusCode(HttpURLConnection.HTTP_OK)
                    .log().everything()
                .when()
                    .get("/rest/geolocation")
                .then()
                    .body("status", equalTo("ok"))
        ;

        geocontainer.shutdown();
    }

}
