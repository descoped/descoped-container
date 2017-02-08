package io.descoped.container.cdi.stage;

import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.api.projectstage.ProjectStageHolder;

/**
 * Created by oranheim on 29/12/2016.
 */
public class DaemonTestProjectStageHolder implements ProjectStageHolder {

    public static final class DaemonTest extends ProjectStage  {
        private static final long serialVersionUID = -5556812956087205063L;
    }

    public static final DaemonTest DaemonTest = new DaemonTest();
}
