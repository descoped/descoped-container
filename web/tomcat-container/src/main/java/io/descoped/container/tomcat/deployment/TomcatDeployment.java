package io.descoped.container.tomcat.deployment;

import io.descoped.container.core.Deployment;
import io.descoped.container.core.ServerContainer;
import io.descoped.container.exception.DescopedServerException;
import io.descoped.container.extension.ClassLoaders;
import io.descoped.container.tomcat.core.TomcatContainer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Constants;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by oranheim on 03/02/2017.
 */
public class TomcatDeployment implements Deployment {

    private static final Logger log = LoggerFactory.getLogger(TomcatDeployment.class);
    private TomcatWebApp webapp;

    public void register(TomcatWebApp webapp) {
        this.webapp = webapp;
    }

    @Override
    public void deploy(ServerContainer container) {
        TomcatContainer server = (TomcatContainer) container;
        Tomcat tomcat = server.builder();

        tomcat.setBaseDir("./target");
        {
            Path target = Paths.get("./target").resolve("webapps/ROOT");
            try {
                Files.createDirectories(target);
            } catch (IOException e) {
                throw new DescopedServerException(e);
            }
        }
        tomcat.getServer().setParentClassLoader(ClassLoaders.tccl());

        StandardContext context = webapp.getStandardContext();

        StandardJarScanner standardJarScanner = new StandardJarScanner();
        standardJarScanner.setScanBootstrapClassPath(false);
        standardJarScanner.setScanClassPath(false);
        standardJarScanner.setScanAllDirectories(false);
        context.setJarScanner(standardJarScanner);

        context.addLifecycleListener(tomcat.getDefaultWebXmlListener());

        ContextConfig contextConfig = new ContextConfig();
        context.addLifecycleListener(contextConfig);
        contextConfig.setDefaultWebXml(Constants.NoDefaultWebXml);

        try {
            String webAppResources = new File("src/main/resources/webapp").getAbsolutePath();
            log.trace("ContextPath: {} -- WebAppResources: {}", server.getContextPath(), webAppResources);
            Context resourceContext = tomcat.addWebapp(webapp.getStandardContext().getPath(), webAppResources);
//            tomcat.addContext("/MyWebApp/images", "/tmp/images/");

            WebResourceRoot resources = new StandardRoot(resourceContext);
            context.setResources(resources);

        } catch (ServletException e) {
            throw new DescopedServerException(e);
        }



        tomcat.getHost().addChild(context);
    }

    @Override
    public void undeploy(ServerContainer container) {
        TomcatContainer server = (TomcatContainer) container;
        try {
            server.builder().getServer().destroy();
        } catch (LifecycleException e) {
            throw new DescopedServerException(e);
        }
    }

}
