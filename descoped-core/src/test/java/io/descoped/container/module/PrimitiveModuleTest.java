package io.descoped.container.module;

import io.descoped.container.module.spi.CdiContainerModule;
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
        PrimitiveDiscovery primitiveDiscovery = PrimitiveDiscovery.getModuleInstances();
        Map<Class<DescopedPrimitive>, DescopedPrimitive> map = primitiveDiscovery.obtain();

        assertEquals(1, map.size());

        Iterator<Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive>> accedingIt = map.entrySet().iterator();
        Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive> entry;

        assertTrue(accedingIt.hasNext());
        entry = accedingIt.next();
        assertEquals(CdiContainerModule.class, entry.getKey());
    }

    @Test
    public void testPrimitiveDiscoveryShutdownOrder() throws Exception {
        PrimitiveDiscovery primitiveDiscovery = PrimitiveDiscovery.getModuleInstances();
        Map<Class<DescopedPrimitive>, DescopedPrimitive> map = primitiveDiscovery.obtain();

        assertEquals(1, map.size());

        ListIterator<Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive>> decendingIt = primitiveDiscovery.reverseOrder();
        Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive> entry;

        assertTrue(decendingIt.hasPrevious());
        entry = decendingIt.previous();
        assertEquals(CdiContainerModule.class, entry.getKey());
        primitiveDiscovery.removePrimitive(entry.getKey());

        assertEquals(0, map.size());
    }

}
