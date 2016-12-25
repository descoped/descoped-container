package io.descoped.server.extension;

import io.descoped.server.container.UndertowContainer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Created by oranheim on 23/12/2016.
 */
@ApplicationScoped
public class ServerProducer {

//    @Produces
//    @WebServer
    public UndertowContainer produceWebServerContainer(InjectionPoint injectionPoint) {
        String ipName = injectionPoint.getMember().getDeclaringClass().getName();
//        ServerExtension serverExtension = BeanManagerProvider.getInstance().getBeanManager().getExtension(ServerExtension.class);
        return new UndertowContainer(null);
    }

}
