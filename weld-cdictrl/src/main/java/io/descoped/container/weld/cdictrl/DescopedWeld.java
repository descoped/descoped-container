package io.descoped.container.weld.cdictrl;

import org.jboss.weld.bootstrap.api.CDI11Bootstrap;
import org.jboss.weld.bootstrap.api.Service;
import org.jboss.weld.bootstrap.spi.Deployment;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.resources.spi.ResourceLoader;
import org.jboss.weld.transaction.spi.TransactionServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by oranheim on 06/02/2017.
 */
public class DescopedWeld extends Weld {

    private static final Logger log = LoggerFactory.getLogger(DescopedWeld.class);

    public DescopedWeld() {
        super();
    }

    public DescopedWeld(String containerId) {
        super(containerId);
    }

    private Class<? extends Service> resolveTransactionServicesClass() {
        try {
            Class<? extends Service> clazz = (Class<? extends Service>) Class.forName("io.descoped.support.jta.DescopedTransactionServices");
            return clazz;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private <S extends Service> S newTransactionServicesInstance(Class<? extends Service> txClass) {
        try {
            return (S) txClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Deployment createDeployment(ResourceLoader resourceLoader, CDI11Bootstrap bootstrap) {
        Deployment deployment = super.createDeployment(resourceLoader, bootstrap);
        Class<? extends Service> txClass = resolveTransactionServicesClass();
        if (txClass != null) {
            log.trace("Register Service: {}", txClass);
            deployment.getServices().add(TransactionServices.class, newTransactionServicesInstance(txClass));
        }
        return deployment;
    }

}
