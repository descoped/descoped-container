package io.descoped.hystrix.commands;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

/**
 * Created by oranheim on 30/12/2016.
 */
abstract public class BaseHystrixCommand<R> extends HystrixCommand<R> {

    private static final Logger log = LoggerFactory.getLogger(BaseHystrixCommand.class);
    private OutputStream output;

    public BaseHystrixCommand(String hystrixGroupKey, int hystrixExecutionTimeOut) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(hystrixGroupKey)).
                andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(hystrixExecutionTimeOut)));
        HystrixRequestContext.initializeContext();
    }

}
