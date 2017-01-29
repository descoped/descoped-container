package io.descoped.container.module;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by oranheim on 27/01/2017.
 */
@ApplicationScoped
public class BarPrimitive implements DescopedPrimitive {

    @Override
    public void init() {

    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        System.out.println("--------------------> Is BarPrimitive Running? " + isRunning());
    }

    @Override
    public void destroy() {

    }
}
