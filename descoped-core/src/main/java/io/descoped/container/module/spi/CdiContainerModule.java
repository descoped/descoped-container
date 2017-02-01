package io.descoped.container.module.spi;

import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;

/**
 * Created by oranheim on 28/01/2017.
 */
@Priority(100)
@PrimitiveModule
public class CdiContainerModule implements DescopedPrimitive {

    private static final Logger log = LoggerFactory.getLogger(CdiContainerModule.class);
    private CdiContainer cdiContainer;

    @Override
    public void init() {
        cdiContainer = CdiContainerLoader.getCdiContainer();
    }

    @Override
    public void start() {
        log.trace("start");
        cdiContainer.boot();
        cdiContainer.getContextControl().startContext(ApplicationScoped.class);
    }

    @Override
    public void stop() {
        log.trace("stop");
        cdiContainer.getContextControl().stopContext(ApplicationScoped.class);
        cdiContainer.shutdown();
    }

    @Override
    public void destroy() {
        cdiContainer = null;
    }

}
