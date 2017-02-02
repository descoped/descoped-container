package io.descoped.container.jetty.core;

import io.descoped.container.core.ServerContainer;

/**
 * Created by oranheim on 03/02/2017.
 */
public class JettyContainer extends ServerContainer {

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isStopped() {
        return false;
    }

}
