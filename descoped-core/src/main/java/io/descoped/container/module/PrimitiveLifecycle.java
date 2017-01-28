package io.descoped.container.module;

/**
 * Created by oranheim on 28/01/2017.
 */
public class PrimitiveLifecycle implements DescopedPrimitive {

    private volatile DescopedPrimitive delegate;
    private boolean running;

    public PrimitiveLifecycle(DescopedPrimitive delegate) {
        this.delegate = delegate;
    }

    public boolean isRunning() {
        return running;
    }

    public Class<? extends DescopedPrimitive> internalClass() {
        return delegate.getClass();
    }

    @Override
    public void init() {
        delegate.init();
    }

    @Override
    public void start() {
        delegate.start();
        running = true;
    }

    @Override
    public void stop() {
        delegate.stop();
        running = false;
    }

    @Override
    public void destroy() {
        delegate.destroy();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }


}
