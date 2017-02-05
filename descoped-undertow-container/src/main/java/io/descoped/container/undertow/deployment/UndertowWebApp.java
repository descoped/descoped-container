package io.descoped.container.undertow.deployment;

import io.descoped.container.deployment.spi.Filter;
import io.descoped.container.deployment.spi.Servlet;
import io.descoped.container.deployment.spi.WebApp;
import io.descoped.container.extension.ClassLoaders;
import io.descoped.container.info.InfoBuilder;
import io.descoped.container.undertow.core.CDIClassIntrospecter;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EventListener;

/**
 * Created by oranheim on 23/01/2017.
 */
public class UndertowWebApp implements WebApp<UndertowWebApp> {

    private static final Logger log = LoggerFactory.getLogger(UndertowWebApp.class);
    private final DeploymentInfo deploymentInfo;
    private final StringBuilder infoBuilderOld;
    protected static ThreadLocal<Integer> listenerCount = ThreadLocal.withInitial(() -> 0);
    protected InfoBuilder infoBuilder = InfoBuilder.builder();

    public UndertowWebApp() {
        UndertowWebApp.listenerCount.remove();
        UndertowServlet.servletCount.remove();
        UndertowFilter.filterCount.remove();


        infoBuilderOld = new StringBuilder();
        ClassLoader classLoader = ClassLoaders.tccl();
        deploymentInfo = Servlets.deployment();
        deploymentInfo.setClassIntrospecter(CDIClassIntrospecter.INSTANCE);
        deploymentInfo.setClassLoader(classLoader);
    }

    public DeploymentInfo getDeploymentInfo() {
        return deploymentInfo;
    }

    @Override
    public UndertowWebApp name(String name) {
        infoBuilder.keyValue("name", name);
        deploymentInfo.setDeploymentName(name);
        return this;
    }

    @Override
    public UndertowWebApp contextPath(String contextPath) {
        infoBuilder.keyValue("contextPath", contextPath);
        deploymentInfo.setContextPath(contextPath);
        return this;
    }

    @Override
    public UndertowWebApp addListener(Class<? extends EventListener> listenerClass) {
        infoBuilder.keyValue("addListener" + listenerCount.get(), listenerClass.getName()); listenerCount.set(listenerCount.get().intValue()+1);
        deploymentInfo.addListener(Servlets.listener(listenerClass));
        return this;
    }

    @Override
    public Filter<UndertowWebApp> addFilter(String filterName, Class<? extends javax.servlet.Filter> filterClass) {
        return new UndertowFilter(this, filterName, filterClass);
    }

    @Override
    public Servlet<UndertowWebApp> addServlet(String servletName, Class<? extends javax.servlet.Servlet> servlet) {
        return new UndertowServlet(this, servletName, servlet);
    }

    @Override
    public UndertowWebApp setEagerFilterInit(boolean eagerFilterInit) {
        infoBuilder.keyValue("eagerFilterInit", String.valueOf(eagerFilterInit));
        deploymentInfo.setEagerFilterInit(eagerFilterInit);
        return this;
    }

    @Override
    public UndertowWebApp addWelcomePage(String welcomePage) {
        infoBuilder.keyValue("welcomePage", welcomePage);
        deploymentInfo.addWelcomePage(welcomePage);
        return this;
    }

    @Override
    public String info() {
        return infoBuilder.build();
    }

    @Override
    public UndertowWebApp build() {
        throw new UnsupportedOperationException();
    }
}
