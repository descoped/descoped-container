package io.descoped.container.module;

import io.descoped.container.log.ConsoleAppender;
import io.descoped.container.module.cdi.CdiInstanceFactory;
import io.descoped.container.module.factory.InstanceFactory;
import io.descoped.container.module.factory.InstanceHandler;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@RunWith(CdiTestRunner.class)
@TestControl(logHandler = ConsoleAppender.class)
public class PrimitiveTest {

    private static final Logger log = LoggerFactory.getLogger(PrimitiveTest.class);

    @Test
    public void testPrimitiveDiscoveryStartOrder() throws Exception {
        InstanceFactory<DescopedPrimitive> instanceFactory = new CdiInstanceFactory<>(DescopedPrimitive.class);
        Map<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> map = instanceFactory.load();

        assertEquals(3, map.size());

        Iterator<Map.Entry<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>>> accedingIt = map.entrySet().iterator();
        Map.Entry<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> entry;

        assertTrue(accedingIt.hasNext());
        entry = accedingIt.next();
        assertEquals(BazPrimitive.class, entry.getKey());

        assertTrue(accedingIt.hasNext());
        entry = accedingIt.next();
        assertEquals(BarPrimitive.class, entry.getKey());

        assertTrue(accedingIt.hasNext());
        entry = accedingIt.next();
        assertEquals(FooPrimitive.class, entry.getKey());
    }

    @Test
    public void testPrimitiveDiscoveryShutdownOrder() throws Exception {
        InstanceFactory<DescopedPrimitive> instanceFactory = new CdiInstanceFactory<>(DescopedPrimitive.class);
        Map<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> map = instanceFactory.load();

        assertEquals(3, map.size());

        ListIterator<Map.Entry<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>>> decendingIt = instanceFactory.reverseOrder();
        Map.Entry<Class<DescopedPrimitive>, InstanceHandler<DescopedPrimitive>> entry;

        assertTrue(decendingIt.hasPrevious());
        entry = decendingIt.previous();
        assertEquals(FooPrimitive.class, entry.getKey());
        instanceFactory.remove(entry.getKey());

        assertTrue(decendingIt.hasPrevious());
        entry = decendingIt.previous();
        assertEquals(BarPrimitive.class, entry.getKey());
        instanceFactory.remove(entry.getKey());

        assertTrue(decendingIt.hasPrevious());
        entry = decendingIt.previous();
        assertEquals(BazPrimitive.class, entry.getKey());
        instanceFactory.remove(entry.getKey());

        assertEquals(0, map.size());
    }

}
