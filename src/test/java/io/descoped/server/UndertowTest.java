package io.descoped.server;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import org.apache.deltaspike.cdise.servlet.CdiServletRequestListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by oranheim on 11/12/2016.
 */
public class UndertowTest {

    private static final Logger log = LoggerFactory.getLogger(UndertowTest.class);
    private Undertow server;

    @Before
    public void setUp() throws Exception {
//        ServletInfo servletInfo = Servlets.servlet("YourServletName", YourServlet.class).setAsyncSupported(true)
//                .setLoadOnStartup(1).addMapping("/*");
        ListenerInfo listenerInfo = Servlets.listener(CdiServletRequestListener.class);
        DeploymentInfo di = new DeploymentInfo()
                .addListener(listenerInfo)
                .setContextPath("/")
//                .addServlet(servletInfo)
                .setDeploymentName("CdiSEServlet")
                .setClassLoader(ClassLoader.getSystemClassLoader());
        DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(di);
        deploymentManager.deploy();
        server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(deploymentManager.start())
                .build();

        log.info("Start..");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.debug("ShutdownHook triggered..");
                try {
                    if (server != null) server.stop();
                } catch (Exception e) {
                }
            }
        });

        server.start();
    }
    
    @After
    public void tearDown() throws Exception {
        log.info("Stop..");
        server.stop();
        log.info("Shutdown!");
    }

    @Test
    public void testRest() throws Exception {
        log.info("Read..");
//        System.in.read();
        log.info("Done reading!");
    }

}
