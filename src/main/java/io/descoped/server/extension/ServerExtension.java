package io.descoped.server.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

/**
 * Created by oranheim on 22/12/2016.
 *
 * This extension implements CDI Lifecylce support for Server containers
 */
public class ServerExtension implements Extension {

    private static final Logger log = LoggerFactory.getLogger(ServerExtension.class);

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
        log.info("beginning the scanning process");
    }


}
