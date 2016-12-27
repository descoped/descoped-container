package io.descoped.server.container;

import org.apache.deltaspike.core.api.config.ConfigResolver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oranheim on 12/12/2016.
 */
abstract public class ServerContainer implements Serializable {

    private String host;
    private int port;
    private static String contextPath;
    private String[] jaxRsPackages;
    private List<Deployment> deployments = new ArrayList<>();

    public ServerContainer() {
        host = ConfigResolver.getPropertyValue("descoped.server.host", "0.0.0.0");
        port = Integer.valueOf(ConfigResolver.getPropertyValue("descoped.server.port", "8080"));
        contextPath = ConfigResolver.getPropertyValue("descoped.server.contextPath", "/");
        jaxRsPackages = ConfigResolver.getPropertyValue("descoped.server.jaxRsPackages", "io.descoped").split(",");
    }

    public ServerContainer(ServerContainer owner) {
        this.copyConfiguration(owner);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static String getContextPath() {
        return contextPath;
    }

    public static void setContextPath(String contextPath) {
        ServerContainer.contextPath = contextPath;
    }

    public String[] getJaxRsPackages() {
        return jaxRsPackages;
    }

    public void setJaxRsPackages(String... jaxRsPackages) {
        this.jaxRsPackages = jaxRsPackages;
    }

    public List<Deployment> getDeployments() {
        return deployments;
    }

    public void deploy(Deployment deployment) {
        deployment.deploy(this);
        this.deployments.add(deployment);
    }

    public void undeploy(Deployment deployment) {
        deployment.undeploy(this);
        this.deployments.remove(deployment);
    }

    abstract public void start();
    abstract public void shutdown();
    abstract public boolean isRunning();
    abstract public boolean isStopped();

    public void copyConfiguration(ServerContainer that) {
        if (that == null) return;
        this.setHost(that.getHost());
        this.setPort(that.getPort());
        this.setContextPath(that.getContextPath());
        this.setJaxRsPackages(that.getJaxRsPackages());
    }

}
