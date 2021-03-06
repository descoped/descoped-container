package io.descoped.container.cdi.support;

import javax.inject.Qualifier;
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
@Qualifier
@Documented
public @interface Descoped {
}
