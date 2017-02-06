package io.descoped.container.deployment.spi;

import java.util.EventListener;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by oranheim on 23/01/2017.
 */
public interface WebApp<T extends WebApp> {

    T name(String name);

    T contextPath(String contextPath);

    T addListener(Class<? extends EventListener> listenerClass);

    Filter<T> addFilter(String filterName, Class<? extends javax.servlet.Filter> filterClass);

    Servlet<T> addServlet(String servletName, Class<? extends javax.servlet.Servlet> servlet);

    T setEagerFilterInit(boolean eagerFilterInit);

    T addWelcomePage(String welcomePage);

    String info();

    T build();

    static <T> T create(Class<T> provider) {
        return (T) create();
    }

    static WebApp create() {
        ServiceLoader<WebApp> serviceLoader = ServiceLoader.load(WebApp.class);
        Iterator<WebApp> it = serviceLoader.iterator();
        if (it.hasNext()) {
            try {
                WebApp webapp = it.next();
                Class<?> clazz = webapp.getClass();
                return (WebApp) clazz.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("Unable to obtain WebApp Deployment SPI", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to obtain WebApp Deployment SPI", e);
            }
        }
        throw new RuntimeException("Unable to locate WebApp Deployment SPI");
    }

}
