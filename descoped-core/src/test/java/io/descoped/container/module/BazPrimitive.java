package io.descoped.container.module;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;

/**
 * Created by oranheim on 28/01/2017.
 */
@Priority(5)
@ApplicationScoped
public class BazPrimitive implements DescopedPrimitive {
    @Override
    public void init() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }
}
