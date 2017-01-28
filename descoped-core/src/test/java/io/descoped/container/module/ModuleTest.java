package io.descoped.container.module;

import io.descoped.container.log.ConsoleAppender;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.spi.CDI;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by oranheim on 27/01/2017.
 */
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class ModuleTest {

    private static final Logger log = LoggerFactory.getLogger(ModuleTest.class);

    @Test
    public void testPrimitiveDiscoveryStartOrder() throws Exception {
        PrimitiveDiscovery primitiveDiscovery = CDI.current().select(PrimitiveDiscovery.class).get();
        Map<Class<DescopedPrimitive>, DescopedPrimitive> map = primitiveDiscovery.obtain();

        assertEquals(3, map.size());

        Iterator<Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive>> accedingIt = map.entrySet().iterator();
        Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive> entry;

        assertTrue(accedingIt.hasNext());
        entry = accedingIt.next();
        assertEquals(BazModule.class, entry.getKey());

        assertTrue(accedingIt.hasNext());
        entry = accedingIt.next();
        assertEquals(BarModule.class, entry.getKey());

        assertTrue(accedingIt.hasNext());
        entry = accedingIt.next();
        assertEquals(FooModule.class, entry.getKey());
    }

    @Test
    public void testPrimitiveDiscoveryShutdownOrder() throws Exception {
        PrimitiveDiscovery primitiveDiscovery = CDI.current().select(PrimitiveDiscovery.class).get();
        Map<Class<DescopedPrimitive>, DescopedPrimitive> map = primitiveDiscovery.obtain();

        assertEquals(3, map.size());

        ListIterator<Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive>> decendingIt = primitiveDiscovery.reverseOrder();
        Map.Entry<Class<DescopedPrimitive>, DescopedPrimitive> entry;

        assertTrue(decendingIt.hasPrevious());
        entry = decendingIt.previous();
        assertEquals(FooModule.class, entry.getKey());
        primitiveDiscovery.removePrimitive(entry.getKey());

        assertTrue(decendingIt.hasPrevious());
        entry = decendingIt.previous();
        assertEquals(BarModule.class, entry.getKey());
        primitiveDiscovery.removePrimitive(entry.getKey());

        assertTrue(decendingIt.hasPrevious());
        entry = decendingIt.previous();
        assertEquals(BazModule.class, entry.getKey());
        primitiveDiscovery.removePrimitive(entry.getKey());

        assertEquals(0, map.size());
    }

}