package io.descoped.container.module;

import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.factory.InstanceHandler;
import io.descoped.container.module.spi.CdiContainerModule;
import io.descoped.container.module.spi.SpiInstanceFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by oranheim on 27/01/2017.
 */
@RunWith(JUnit4.class)
//@TestControl(logHandler = ConsoleAppender.class)
public class PrimitiveModuleTest {

    private static final Logger log = LoggerFactory.getLogger(PrimitiveModuleTest.class);

    @Test
    public void testPrimitiveDiscoveryStartOrder() throws Exception {
        InstanceFactory<DescopedPrimitive> instanceFactory = new SpiInstanceFactory<>(DescopedPrimitive.class);
        Map<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> map = instanceFactory.load();

        assertEquals(1, map.size());

        Iterator<Map.Entry<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>>> accedingIt = map.entrySet().iterator();
        Map.Entry<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> entry;

        assertTrue(accedingIt.hasNext());
        entry = accedingIt.next();
        assertEquals(CdiContainerModule.class, entry.getKey());
    }

    @Test
    public void testPrimitiveDiscoveryShutdownOrder() throws Exception {
        InstanceFactory<DescopedPrimitive> instanceFactory = new SpiInstanceFactory<>(DescopedPrimitive.class);
        Map<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> map = instanceFactory.load();

        assertEquals(1, map.size());

        ListIterator<Map.Entry<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>>> decendingIt = instanceFactory.reverseOrder();
        Map.Entry<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> entry;

        assertTrue(decendingIt.hasPrevious());
        entry = decendingIt.previous();
        assertEquals(CdiContainerModule.class, entry.getKey());
        instanceFactory.remove(entry.getKey());

        assertEquals(0, map.size());
    }

}
