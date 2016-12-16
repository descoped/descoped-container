package io.descoped.server.container;

/**
 * Created by oranheim on 12/12/2016.
 */
abstract public class ServerContainer {

    private String host = "0.0.0.0";
    private int port = 8080;
    private static String contextPath = "/";
    private String[] jaxRsPackages = {"io.descoped"};

    public ServerContainer() {
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

    abstract public void start();
    abstract public void shutdown();
    abstract public boolean isRunning();
    abstract public boolean isStopped();

    public void copyConfiguration(ServerContainer that) {
        this.setHost(that.getHost());
        this.setPort(that.getPort());
        this.setContextPath(that.getContextPath());
        this.setJaxRsPackages(that.getJaxRsPackages());
    }

}
