package io.descoped.server.config;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.util.ProjectStageProducer;

import java.util.Random;

/**
 * Created by oranheim on 29/12/2016.
 */
public class ContainerConfig {

    public static String getHost() {
        return ConfigResolver.getPropertyValue("descoped.server.host", "0.0.0.0");
    }

    public static int getPort() {
        return (isTestProjectStage() ?
                new Random().nextInt(150) + 1 + 9000 :
                ConfigResolver.resolve("descoped.server.port").as(Integer.class).withDefault(8080).getValue()
        );
    }

    public static String getContextPath() {
        return ConfigResolver.getPropertyValue("descoped.server.contextPath", "/");
    }

    public static String[] getScanJaxRsPackages() {
        return ConfigResolver.getPropertyValue("descoped.server.jaxRsPackages", "io.descoped").split(",");
    }

    public static int getMaxWorkers() {
        return ConfigResolver.resolve("descoped.server.maxWorkers").as(Integer.class).withDefault(0).getValue();
    }

    private static boolean isTestProjectStage() {
        ProjectStage stage = ProjectStageProducer.getInstance().getProjectStage();
        return ProjectStage.UnitTest.equals(stage)
                || ProjectStage.IntegrationTest.equals(stage);
    }

}
