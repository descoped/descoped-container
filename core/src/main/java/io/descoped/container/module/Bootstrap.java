package io.descoped.container.module;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Created by oranheim on 19/01/2017.
 */
public final class Bootstrap {
    private static final Logger LOG = Logger.getLogger(Bootstrap.class.getSimpleName());

    public static final Bootstrap SINGLETON = new Bootstrap();

    public Bootstrap() {
        String time = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
//        LOG.info(String.format("%s [%s] INFO  Descoped bootstrap..", time, Thread.currentThread().getName()));
        System.out.println(String.format("%s [%s] INFO  Descoped bootstrap..", time, Thread.currentThread().getName()));

        Path currentPath = Paths.get("").toAbsolutePath();
        String filename = currentPath.toString() + "/src/main/resources/logging.properties";
        if (new File(filename).exists()) {
            System.setProperty("java.util.logging.config", filename);
            System.out.println("---------------------> Set logging props: " + filename);
        }
    }

    public void init() {
        // nop
    }
}
