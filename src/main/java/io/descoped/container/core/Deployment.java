package io.descoped.container.core;

import org.apache.deltaspike.core.api.exclude.Exclude;

/**
 * Created by oranheim on 19/12/2016.
 */
@Exclude
public interface Deployment {

    void deploy(ServerContainer container);

    void undeploy(ServerContainer container);

}
