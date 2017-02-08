package io.descoped.container.core;

import io.descoped.container.cdi.factory.CdiInstanceFactory;
import io.descoped.container.cdi.log.ConsoleAppender;
import io.descoped.container.module.DescopedPrimitive;
import io.descoped.container.module.factory.DefaultInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.factory.InstanceHandler;
import io.descoped.container.module.spi.SpiInstanceFactory;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by oranheim on 31/01/2017.
 */
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class PrimitiveLoaderTest {

    private final static Logger log = LoggerFactory.getLogger(PrimitiveLoaderTest.class);

    @Test
    public void testCdiLoader() throws Exception {
        try {
            InstanceFactory<DescopedPrimitive> factory = DefaultInstanceFactory.get(CdiInstanceFactory.class);

            Map<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> instances = factory.load();
            instances.forEach((k, v) -> {
                log.trace("CDI - Key: {} => Value: {}", k, v.get());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSpiLoader() throws Exception {
        InstanceFactory<DescopedPrimitive> factory = DefaultInstanceFactory.get(SpiInstanceFactory.class);

        Map<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> instances = factory.load();
        instances.forEach((k,v) -> {
            log.trace("SPI - Key: {} => Value: {}", k, v.get());
        });
    }

}
