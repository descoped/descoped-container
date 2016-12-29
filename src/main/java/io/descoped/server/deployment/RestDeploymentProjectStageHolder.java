package io.descoped.server.deployment;

import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.api.projectstage.ProjectStageHolder;

/**
 * Created by oranheim on 29/12/2016.
 */
public class RestDeploymentProjectStageHolder implements ProjectStageHolder {

    public static final class RestDeployment extends ProjectStage  {
        private static final long serialVersionUID = -5556812956087205063L;
    }

    public static final RestDeployment RestDeployment = new RestDeployment();
}
