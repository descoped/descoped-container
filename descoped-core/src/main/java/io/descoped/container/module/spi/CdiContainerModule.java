package io.descoped.container.module.spi;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;

/**
 * Created by oranheim on 28/01/2017.
 */
@Priority(100)
@PrimitiveModule
public class CdiContainerModule implements DescopedPrimitive {

    private static final Logger log = LoggerFactory.getLogger(CdiContainerModule.class);

    @Override
    public void init() {

    }

    @Override
    public void start() {
        log.trace("start");
    }

    @Override
    public void stop() {
        log.trace("stop");
    }

    @Override
    public void destroy() {

    }

}
