package io.descoped.container.commands.geolocation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import io.descoped.container.commands.BaseHttpGetHystrixCommand;
import io.descoped.container.support.DescopedServerException;

import java.io.IOException;
import java.net.URI;

/**
 * Created by oranheim on 30/12/2016.
 */
public class CommandGetGeoLocation extends BaseHttpGetHystrixCommand<GeoLocation> {

    protected CommandGetGeoLocation() {
        super(URI.create("http://www.geoplugin.net"), "GEO", 10000);
    }

    @Override
    protected String getTargetPath() {
        return "/json.gp";
    }

    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
        return request;
    }

    @Override
    protected GeoLocation dealWithResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            GeoLocation location = mapper.readValue(response, GeoLocation.class);
            return location;
        } catch (JsonParseException e) {
            throw new DescopedServerException(e);
        } catch (JsonMappingException e) {
            throw new DescopedServerException(e);
        } catch (IOException e) {
            throw new DescopedServerException(e);
        }
    }
}
