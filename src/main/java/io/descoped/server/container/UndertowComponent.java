package io.descoped.server.container;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.util.UUID;

/**
 * Created by oranheim on 19/12/2016.
 */
@ApplicationScoped
@DeploymentEvent
public class UndertowComponent implements Dep {

    @Override
    public String val() {
        return UUID.randomUUID().toString();
    }

}
