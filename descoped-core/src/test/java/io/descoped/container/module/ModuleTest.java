package io.descoped.container.module;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * Created by oranheim on 28/01/2017.
 */
public class ModuleTest {

    private static final Logger log = LoggerFactory.getLogger(ModuleTest.class);

    @Test
    public void testLoadModules() throws Exception {
        ServiceLoader<DescopedModule> modules = ServiceLoader.load(DescopedModule.class);
        modules.forEach(module -> {
            log.trace("---> {}", module);

        });
    }

}
