package io.descoped.container.module;

/**
 * Created by oranheim on 27/01/2017.
 */
public interface DescopedPrimitive {

    void init();

    void start();

    void stop();

    void destroy();

    default boolean isRunning() {
        return isRunning(this);
    }

    static boolean isRunning(DescopedPrimitive descopedPrimitive) {
        Class<? extends DescopedPrimitive> clazz = (descopedPrimitive instanceof PrimitiveLifecycle ?
                ((PrimitiveLifecycle) descopedPrimitive).internalClass() : descopedPrimitive.getClass());
        PrimitiveLifecycle primitive = DescopedContainer.findPrimitiveLifecycleInstance(clazz);
        if (primitive != null) {
            return primitive.isRunning();
        }
        throw new IllegalStateException("Unable to locate primitive class: " + descopedPrimitive);
    }

}
