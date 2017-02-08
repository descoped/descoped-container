package io.descoped.container.info;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by oranheim on 03/02/2017.
 */
@RunWith(JUnit4.class)
public class InfoBuilderTest {

    private static final Logger log = LoggerFactory.getLogger(InfoBuilderTest.class);

    @Test
    public void testMe() throws Exception {
        InfoBuilder builder = InfoBuilder.builder();

        builder
                .keyValue("k1", "v1")               // L0
                .key("k2")                          // L0
                    .keyValue("k3", "v3")           // L1
                    .keyValue("k4", "v4")           // L1
                    .key("k2.1")                    // L1
                        .keyValue("k3.1", "v3.1")   // L2
                        .up()                       // L1
                    .keyValue("k2.2", "v2.2")
                    .up()                           // L0
                .keyValue("k5", "v5")               // L0
                .keyValue("k6", "v6")               // L0
        ;

        builder.dump();
        log.debug("JSON:\n{}", builder.build());
    }
}
