package io.descoped.container.module.spi;

import io.descoped.container.module.DescopedModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;

/**
 * Created by oranheim on 28/01/2017.
 */
@Priority(100)
public class CdiContainerModule implements DescopedModule {

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
