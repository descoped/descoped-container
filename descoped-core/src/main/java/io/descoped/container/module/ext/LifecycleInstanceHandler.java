package io.descoped.container.module.ext;

/**
 * Created by oranheim on 01/02/2017.
 */
abstract public class LifecycleInstanceHandler<T> implements InstanceHandler<T> {

    private boolean running = false;

    @Override
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
