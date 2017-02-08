package io.descoped.container.cdi.util;

import io.descoped.container.cdi.stage.DaemonTestProjectStageHolder;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.apache.deltaspike.core.util.ProjectStageProducer;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by oranheim on 19/01/2017.
 */
public class CdiUtil {

    public static List<Class<?>> classesFor(Class<?> clazz) {
        BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
        Set<Bean<?>> beans = beanManager.getBeans(clazz);
        List<Class<?>> classes = beans.stream().map(Bean::getBeanClass).collect(Collectors.toList());
        return classes;
    }

    public static <T> T instanceOf(Class<T> beanClass) {
        BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
        Bean<T> bean = (Bean<T>) beanManager.resolve(beanManager.getBeans(beanClass));
        return beanManager.getContext(bean.getScope()).get(bean, beanManager.createCreationalContext(bean));
    }

    /**
     * Obtain a reference to produced beans
     *
     * @param beanClass
     * @param annotationLiterals
     * @param <T>
     * @return
     */
    public static <T> T instanceOf(Class<T> beanClass, AnnotationLiteral... annotationLiterals) {
        BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
        Bean bean = beanManager.resolve(beanManager.getBeans(beanClass, annotationLiterals));
        CreationalContext creationalContext = beanManager.createCreationalContext(bean);
        T managedInstance = (T) beanManager.getReference(bean, beanClass, creationalContext);
        return managedInstance;
    }

    public static boolean isDaemonTestProjectStage() {
        ProjectStage stage = ProjectStageProducer.getInstance().getProjectStage();
        return !DaemonTestProjectStageHolder.DaemonTest.equals(stage);
    }


}
