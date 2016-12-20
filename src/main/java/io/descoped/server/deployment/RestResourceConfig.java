package io.descoped.server.deployment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by oranheim on 20/12/2016.
 */
public class RestResourceConfig extends ResourceConfig {

    public RestResourceConfig() {
        String scanPackagesConfig = ConfigResolver.getPropertyValue("scanPackages", "io.descoped");
        String[] scanPackages = scanPackagesConfig.split(",");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // create JsonProvider to provide custom ObjectMapper
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(mapper);
        register(provider);

        packages(scanPackages);
    }
}
