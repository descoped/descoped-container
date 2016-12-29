package io.descoped.server.deployment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.descoped.server.config.ContainerConfig;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by oranheim on 20/12/2016.
 */
public class RestResourceConfig extends ResourceConfig {

    public RestResourceConfig() {
        String[] scanPackages = ContainerConfig.getScanJaxRsPackages();;
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // create JsonProvider to provide custom ObjectMapper
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(mapper);
        register(provider);

        packages(scanPackages);
    }

}
