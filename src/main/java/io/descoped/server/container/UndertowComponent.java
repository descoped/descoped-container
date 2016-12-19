package io.descoped.server.container;

import java.util.UUID;

/**
 * Created by oranheim on 19/12/2016.
 */
public class UndertowComponent {

    @DeploymentEvent
    public String me() {
        return UUID.randomUUID().toString();
    }

    @DeploymentEvent
    public String me2() {
        return UUID.randomUUID().toString();
    }

}
