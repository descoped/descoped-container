package io.descoped.container.module.spi;

import io.descoped.container.module.DescopedContainer;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.PrimitiveModule;
import io.descoped.container.module.cdi.CdiInstanceFactory;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;

/**
 * Created by oranheim on 28/01/2017.
 */
@Priority(100)
@PrimitiveModule
public class CdiContainerModule implements DescopedPrimitive {

    private static final Logger log = LoggerFactory.getLogger(CdiContainerModule.class);
    private CdiContainer cdiContainer;
    private InstanceFactory<DescopedPrimitive> instanceFactory;
    private DescopedContainer<DescopedPrimitive> descopedContainer;

    @Override
    public void init() {
        cdiContainer = CdiContainerLoader.getCdiContainer();
        instanceFactory = DefaultInstanceFactory.get(CdiInstanceFactory.class);
        descopedContainer = new DescopedContainer<>(instanceFactory);
    }

    @Override
    public void start() {
        cdiContainer.boot();
//        ContextControl cc = BeanProvider.getContextualReference(ContextControl.class);
//        log.trace("-----------------------> ContextControl: {}", cc);
//        cc.startContext(ApplicationScoped.class);
//        cc.startContext(RequestScoped.class);
        cdiContainer.getContextControl().startContext(ApplicationScoped.class);
        cdiContainer.getContextControl().startContext(RequestScoped.class); // todo: only required for testing when using SPI
        descopedContainer.start();
    }

    @Override
    public void stop() {
        descopedContainer.stop();
        cdiContainer.getContextControl().stopContext(ApplicationScoped.class);
        cdiContainer.shutdown();
    }

    @Override
    public void destroy() {
        descopedContainer = null;
        instanceFactory = null;
        cdiContainer = null;
    }

}
