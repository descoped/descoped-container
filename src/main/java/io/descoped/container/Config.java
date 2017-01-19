package io.descoped.container;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by oranheim on 19/01/2017.
 */
public final class Config {
    private static final Config SINGLETON = new Config();

    static {
        SINGLETON.init();
    }

    private final void init() {
        Path currentPath = Paths.get("").toAbsolutePath();
        String filename = currentPath.toString() + "/src/main/resources/logging.properties";
        if (new File(filename).exists()) {
            System.setProperty("java.util.logging.config", filename);
            System.out.println("---------------------> Set logging props: " + filename);
        }
    }
}
