package io.descoped.container.commands.geolocation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import io.descoped.container.commands.BaseHttpGetHystrixCommand;
import io.descoped.container.support.DescopedServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

/**
 * Created by oranheim on 30/12/2016.
 */
public class CommandGetGeoLocation extends BaseHttpGetHystrixCommand<GeoLocation> {

    private static final Logger log = LoggerFactory.getLogger(CommandGetGeoLocation.class);

    private final String ip;

    protected CommandGetGeoLocation(String ip) {
        super(URI.create("http://www.geoplugin.net"), "GEO", 10000);
        this.ip = ip;
    }

    @Override
    protected String getTargetPath() {
        boolean isLocalhost = (ip == null || "127.0.0.1".equals(ip) || ip.startsWith("192.168"));
        return (isLocalhost || ip == null ? "/json.gp" : "/json.gp?ip=" + ip);
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
