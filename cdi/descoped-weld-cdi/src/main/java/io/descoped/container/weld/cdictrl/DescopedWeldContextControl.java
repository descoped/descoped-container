package io.descoped.container.weld.cdictrl;

import org.apache.deltaspike.cdise.api.ContextControl;
import org.jboss.weld.context.AbstractSharedContext;
import org.jboss.weld.context.ApplicationContext;
import org.jboss.weld.context.bound.BoundConversationContext;
import org.jboss.weld.context.bound.BoundRequestContext;
import org.jboss.weld.context.bound.BoundSessionContext;
import org.jboss.weld.context.bound.MutableBoundRequest;

import javax.enterprise.context.*;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oranheim on 06/02/2017.
 */
@Dependent
@SuppressWarnings("UnusedDeclaration")
public class DescopedWeldContextControl implements ContextControl
{
    private static ThreadLocal<RequestContextHolder> requestContexts = new ThreadLocal<RequestContextHolder>();
    private static ThreadLocal<Map<String, Object>> sessionMaps = new ThreadLocal<Map<String, Object>>();



    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private BoundSessionContext sessionContext;

    @Inject
    private Instance<BoundRequestContext> requestContextFactory;

    @Inject
    private BoundConversationContext conversationContext;



    @Override
    public void startContexts()
    {
        startApplicationScope();
        startSessionScope();
        startRequestScope();
        startConversationScope(null);
    }

    @Override
    public void startContext(Class<? extends Annotation> scopeClass)
    {
        if (scopeClass.isAssignableFrom(ApplicationScoped.class))
        {
            startApplicationScope();
        }
        else if (scopeClass.isAssignableFrom(SessionScoped.class))
        {
            startSessionScope();
        }
        else if (scopeClass.isAssignableFrom(RequestScoped.class))
        {
            startRequestScope();
        }
        else if (scopeClass.isAssignableFrom(ConversationScoped.class))
        {
            startConversationScope(null);
        }
    }

    /**
     * Stops Conversation, Request and Session contexts.
     * Does NOT stop Application context, only invalidates
     * App scoped beans, as in Weld this context always active and clears
     * automatically on shutdown.
     *
     * {@inheritDoc}
     */
    @Override
    public void stopContexts()
    {
        stopConversationScope();
        stopRequestScope();
        stopSessionScope();
        stopApplicationScope();
    }

    @Override
    public void stopContext(Class<? extends Annotation> scopeClass)
    {
        if (scopeClass.isAssignableFrom(ApplicationScoped.class))
        {
            stopApplicationScope();
        }
        else if (scopeClass.isAssignableFrom(SessionScoped.class))
        {
            stopSessionScope();
        }
        else if (scopeClass.isAssignableFrom(RequestScoped.class))
        {
            stopRequestScope();
        }
        else if (scopeClass.isAssignableFrom(ConversationScoped.class))
        {
            stopConversationScope();
        }
    }

    /*
     * This is a no-op method. In Weld Application Context is active as soon as the container starts
     */
    private void startApplicationScope()
    {
        // No-op, in Weld Application context is always active
    }

    /**
     * Weld Application context is active from container start to its shutdown
     * This method merely clears out all ApplicationScoped beans BUT the context
     * will still be active which may result in immediate re-creation of some beans.
     */
    private void stopApplicationScope()
    {
        // Welds ApplicationContext gets cleaned at shutdown.
        // Weld App context should be always active
        if (applicationContext.isActive())
        {
            // destroys the bean instances, but the context stays active
            applicationContext.invalidate();

            //needed for weld < v1.1.9
            if (applicationContext instanceof AbstractSharedContext)
            {
                ((AbstractSharedContext) applicationContext).getBeanStore().clear();
            }
        }
    }

    void startRequestScope()
    {
        RequestContextHolder rcHolder = requestContexts.get();
        if (rcHolder == null)
        {
            rcHolder = new RequestContextHolder(requestContextFactory.get(), new HashMap<String, Object>());
            requestContexts.set(rcHolder);
            rcHolder.getBoundRequestContext().associate(rcHolder.getRequestMap());
            rcHolder.getBoundRequestContext().activate();
        }
    }

    void stopRequestScope()
    {
        RequestContextHolder rcHolder = requestContexts.get();
        if (rcHolder != null && rcHolder.getBoundRequestContext().isActive())
        {
            rcHolder.getBoundRequestContext().invalidate();
            rcHolder.getBoundRequestContext().deactivate();
            rcHolder.getBoundRequestContext().dissociate(rcHolder.getRequestMap());
            requestContexts.set(null);
            requestContexts.remove();
        }
    }

    private void startSessionScope()
    {
        Map<String, Object> sessionMap = sessionMaps.get();
        if (sessionMap == null)
        {
            sessionMap = new HashMap<String, Object>();
            sessionMaps.set(sessionMap);
        }

        sessionContext.associate(sessionMap);
        sessionContext.activate();

    }

    private void stopSessionScope()
    {
        if (sessionContext.isActive())
        {
            sessionContext.invalidate();
            sessionContext.deactivate();
            sessionContext.dissociate(sessionMaps.get());

            sessionMaps.set(null);
            sessionMaps.remove();
        }
    }

    void startConversationScope(String cid)
    {
        RequestContextHolder rcHolder = requestContexts.get();
        if (rcHolder == null)
        {
            startRequestScope();
            rcHolder = requestContexts.get();
        }
        conversationContext.associate(new MutableBoundRequest(rcHolder.requestMap, sessionMaps.get()));
        conversationContext.activate(cid);
    }

    void stopConversationScope()
    {
        RequestContextHolder rcHolder = requestContexts.get();
        if (rcHolder == null)
        {
            startRequestScope();
            rcHolder = requestContexts.get();
        }
        if (conversationContext.isActive())
        {
            conversationContext.invalidate();
            conversationContext.deactivate();
            conversationContext.dissociate(new MutableBoundRequest(rcHolder.getRequestMap(), sessionMaps.get()));
        }
    }


    private static class RequestContextHolder
    {
        private final BoundRequestContext boundRequestContext;
        private final Map<String, Object> requestMap;

        private RequestContextHolder(BoundRequestContext boundRequestContext, Map<String, Object> requestMap)
        {
            this.boundRequestContext = boundRequestContext;
            this.requestMap = requestMap;
        }

        public BoundRequestContext getBoundRequestContext()
        {
            return boundRequestContext;
        }

        public Map<String, Object> getRequestMap()
        {
            return requestMap;
        }
    }

}
