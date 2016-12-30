package io.descoped.container.core;

import io.descoped.container.config.ContainerConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oranheim on 12/12/2016.
 */
abstract public class ServerContainer implements Serializable {

    private String host;
    private int port;
    private String contextPath;
    private String[] jaxRsPackages;
    private int maxWorkers = 0;
    private List<Deployment> deployments = new ArrayList<>();

    public ServerContainer() {
        configure();
    }

    private void configure() {
        host = ContainerConfig.getHost();
        port = ContainerConfig.getPort();
        contextPath = ContainerConfig.getContextPath();
        jaxRsPackages = ContainerConfig.getScanJaxRsPackages();
        maxWorkers = ContainerConfig.getMaxWorkers();
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

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String[] getJaxRsPackages() {
        return jaxRsPackages;
    }

    public void setJaxRsPackages(String... jaxRsPackages) {
        this.jaxRsPackages = jaxRsPackages;
    }

    public int getMaxWorkers() {
        return maxWorkers;
    }

    public void setMaxWorkers(int maxWorkers) {
        this.maxWorkers = maxWorkers;
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
    }

    abstract public void start();

    abstract public void shutdown();

    abstract public boolean isRunning();

    abstract public boolean isStopped();

}
