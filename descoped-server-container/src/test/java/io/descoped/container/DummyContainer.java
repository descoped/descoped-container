package io.descoped.container;

import io.descoped.container.core.ServerContainer;

/**
 * Created by oranheim on 01/02/2017.
 */
public class DummyContainer extends ServerContainer {

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
