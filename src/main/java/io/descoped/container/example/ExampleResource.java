package io.descoped.container.example;

import io.descoped.container.support.Log;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oranheim on 09/12/2016.
 */
@Path("/test/")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class ExampleResource {

    @Inject
    @Log
    Logger log;

    @GET
    public Map<Object, Object> get() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("foo", "bar");
        log.trace("Produce => foo: bar");
        return map;
    }

}
