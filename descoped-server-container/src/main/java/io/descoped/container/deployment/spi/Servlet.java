package io.descoped.container.deployment.spi;

/**
 * Created by oranheim on 23/01/2017.
 */
public interface Servlet<S extends WebApp> {

    S up();

    Servlet<S> addMapping(String mapping);

    Servlet<S> addInitParam(String name, String value);

    Servlet<S> setAsyncSupported(boolean asyncSupported);

    Servlet<S> setLoadOnStartup(int loadOnStartup);

    Servlet<S> setEnabled(boolean asyncSupported);

}
