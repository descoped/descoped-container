package org.glassfish.jersey.ext.cdi1x.internal;

import org.jboss.weld.Container;

/**
 * Created by oranheim on 28/12/2016.
 */
public class JerseyUtil {

    static void foo() {
        Container.instance().cleanup();
    }
}
