package io.descoped.server.support;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Created by oranheim on 26/12/2016.
 */
public class WebServerLiteral extends AnnotationLiteral<WebServer> implements WebServer {

    private static final long serialVersionUID = 7656638611807582998L;

    private String id;

    public WebServerLiteral(String id) {
//        super();
        this.id = id;
    }

    @Override
    public String id() {        
        return id;
    }

}
