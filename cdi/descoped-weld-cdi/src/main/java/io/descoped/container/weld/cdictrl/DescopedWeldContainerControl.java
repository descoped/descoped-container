package io.descoped.container.weld.cdictrl;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.jboss.weld.Container;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.util.reflection.Formats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.util.Map;
import java.util.Set;

/**
 * Created by oranheim on 06/02/2017.
 */
@SuppressWarnings("UnusedDeclaration")
public class DescopedWeldContainerControl implements CdiContainer {

    private static final Logger LOG = LoggerFactory.getLogger(DescopedWeldContainerControl.class);

    private DescopedWeld weld;
    private WeldContainer weldContainer;

    private Bean<ContextControl> ctxCtrlBean = null;
    private CreationalContext<ContextControl> ctxCtrlCreationalContext = null;
    private ContextControl ctxCtrl = null;


    @Override
    public BeanManager getBeanManager() {
        if (weldContainer == null) {
            return null;
        }

        return weldContainer.getBeanManager();
    }


    @Override
    public synchronized void boot() {
        LOG.trace("Boot Descoped CDI Container");
        weld = new DescopedWeld();
        weldContainer = weld.initialize();
    }

    @Override
    public void boot(Map<?, ?> properties) {
        // no configuration yet. Perform default boot

        boot();
    }

    @Override
    public synchronized void shutdown() {
        if (ctxCtrl != null) {
            try {
                // stops all built-in contexts except for ApplicationScoped as that one is handled by Weld
                ctxCtrl.stopContext(ConversationScoped.class);
                ctxCtrl.stopContext(RequestScoped.class);
                ctxCtrl.stopContext(SessionScoped.class);
                ctxCtrlBean.destroy(ctxCtrl, ctxCtrlCreationalContext);
            } catch (Exception e) {
                // contexts likely already stopped
            }
        }
        try {
            weld.shutdown();
        } catch (Exception e) {
            // something caused weld to shutdown already.
        }
        weld = null;
        ctxCtrl = null;
        ctxCtrlBean = null;
        ctxCtrlCreationalContext = null;

    }

    @Override
    public synchronized ContextControl getContextControl() {
        if (ctxCtrl == null) {
            BeanManager beanManager = getBeanManager();

            if (beanManager == null) {
                LOG.warn("If the CDI-container was started by the environment, you can't use this helper." +
                        "Instead you can resolve ContextControl manually " +
                        "(e.g. via BeanProvider.getContextualReference(ContextControl.class) ). " +
                        "If the container wasn't started already, you have to use CdiContainer#boot before.");

                return null;
            }
            Set<Bean<?>> beans = beanManager.getBeans(ContextControl.class);
            ctxCtrlBean = (Bean<ContextControl>) beanManager.resolve(beans);

            ctxCtrlCreationalContext = getBeanManager().createCreationalContext(ctxCtrlBean);
            LOG.info("o-------------------> ctxCtrlBean: " + ctxCtrlBean.getBeanClass());

            try {
                ctxCtrl = (ContextControl)
                        getBeanManager().getReference(ctxCtrlBean, ContextControl.class, ctxCtrlCreationalContext);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return ctxCtrl;
    }


    @Override
    public String toString() {
        return "WeldContainerControl [Weld " + Formats.version(Container.class.getPackage()) + ']';
    }

}
