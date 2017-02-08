package io.descoped.container.cdi.support;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Created by oranheim on 31/12/2016.
 */
public class DescopedLiteral extends AnnotationLiteral<Descoped> implements Descoped {

    private static final long serialVersionUID = 3526694190212767275L;

    public static final DescopedLiteral INSTANCE = new DescopedLiteral();

}
