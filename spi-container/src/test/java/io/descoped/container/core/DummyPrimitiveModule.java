package io.descoped.container.core;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import io.descoped.container.module.PrimitivePriority;

import javax.annotation.Priority;

/**
 * Created by oranheim on 27/01/2017.
 */
@Priority(PrimitivePriority.FRAMEWORK)
@PrimitiveModule
public class DummyPrimitiveModule implements DescopedPrimitive {

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
