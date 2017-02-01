package io.descoped.container.module;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by oranheim on 27/01/2017.
 */
@ApplicationScoped
public class BarPrimitive implements DescopedPrimitive {

    @Override
    public void init() {
        System.out.println("init: " + getClass().getSimpleName() + " should NOT be running? status: " + this.isRunning());
    }

    @Override
    public void start() {
        System.out.println("start: " + getClass().getSimpleName() + " should NOT be running? status: " + this.isRunning());
    }

    @Override
    public void stop() {
        System.out.println("stop: is " + getClass().getSimpleName() + " running? status: " + this.isRunning() + " -- and is now about to be stopped");
    }

    @Override
    public void destroy() {
        System.out.println("destroy: " + getClass().getSimpleName() + " should NOT be running? status: " + this.isRunning());
    }
}
