package io.descoped.container.module;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Created by oranheim on 11/12/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { TYPE, METHOD, PARAMETER, FIELD })
@Documented
public @interface Primitive {

    boolean waitFor() default false;

}
