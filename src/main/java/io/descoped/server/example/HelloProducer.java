package io.descoped.server.example;

import io.descoped.server.support.MyQualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Created by oranheim on 11/12/2016.
 */
public class HelloProducer {

    @Produces @MyQualifier
    public String produceHelloWorld() {
        return "Hello world";
    }

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}
