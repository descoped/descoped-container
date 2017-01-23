package io.descoped.container.deployment;

import io.descoped.container.deployment.api.WebApp;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by oranheim on 23/01/2017.
 */
public class DeploymentTest {

    @Test
    public void testFluentAPI() throws Exception {
        WebApp webapp = WebApp.create();
        assertNotNull(webapp);
    }

}
