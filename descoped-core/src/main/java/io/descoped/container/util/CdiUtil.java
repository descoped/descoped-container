package io.descoped.container.util;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;

/**
 * Created by oranheim on 19/01/2017.
 */
public class CdiUtil {

    /**
     * Obtain a reference to produced beans
     *
     * @param beanClass
     * @param annotationLiterals
     * @param <T>
     * @return
     */
    public static <T> T instanceOf(Class<T> beanClass, AnnotationLiteral... annotationLiterals) {
        BeanManager beanManager = CDI.current().getBeanManager();
        Bean bean = beanManager.resolve(beanManager.getBeans(beanClass, annotationLiterals));
        CreationalContext creationalContext = beanManager.createCreationalContext(bean);
        T managedInstance = (T) beanManager.getReference(bean, beanClass, creationalContext);
        return managedInstance;
    }

}
