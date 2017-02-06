package io.descoped.container.weld.cdictrl;

//import io.descoped.support.jta.DescopedTransactionServices;

import org.jboss.weld.bootstrap.api.CDI11Bootstrap;
import org.jboss.weld.bootstrap.spi.Deployment;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.resources.spi.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by oranheim on 06/02/2017.
 */
public class DescopedWeld extends Weld {

    private static final Logger log = LoggerFactory.getLogger(DescopedWeld.class);

    public DescopedWeld() {
        log.trace("---------------------------------------------------------> DescopedWeld");
    }

    public DescopedWeld(String containerId) {
        super(containerId);
        log.trace("---------------------------------------------------------> DescopedWeld: {}", containerId);
    }

    @Override
    protected Deployment createDeployment(ResourceLoader resourceLoader, CDI11Bootstrap bootstrap) {
        log.trace("---------------------------------------------------------> createDeployment: {}", resourceLoader);
        Deployment deployment = super.createDeployment(resourceLoader, bootstrap);
//        deployment.getServices().add(TransactionServices.class, new DescopedTransactionServices());
        return deployment;
    }

}
