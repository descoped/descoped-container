package io.descoped.container.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by oranheim on 04/01/2017.
 */
public class CommonUtil {

    private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);
    public static String fileSeparator = System.getProperty("file.separator");
    private static ThreadLocal<OutputStream> outputLocal = new ThreadLocal<OutputStream>() {
        private OutputStream output = null;

        @Override
        protected OutputStream initialValue() {
            if (output == null) {
                output = newOutputStream();
            }
            return output;
        }

        @Override
        public void remove() {
            try {
                output.flush();
                output.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            super.remove();
        }
    };

    public static void info(String msg, Object... values) {
        log.info(msg, values);
    }

    public static void debug(String msg, Object... values) {
        log.debug(msg, values);
    }

    public static void trace(String msg, Object... values) {
        log.trace(msg, values);
    }

    public static void warn(String msg, Object... values) {
        log.warn(msg, values);
    }

    public static void error(String msg, Object... values) {
        log.error(msg, values);
    }

    public static Path getCurrentPath() {
        return Paths.get("").toAbsolutePath();
    }

    public static String currentPath() {
        return getCurrentPath().toString();
    }

    public static void closeOutputStream(OutputStream output) throws IOException {
        output.flush();
        output.close();
    }

    public static OutputStream closeAndCreateNewOutputStream(OutputStream output) throws IOException {
        closeOutputStream(output);
        return newOutputStream();
    }

    public static OutputStream getConsoleOutputStream() {
        return outputLocal.get();
    }

    public static OutputStream newOutputStream() {
        return new OutputStream() {
            private StringBuilder string = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b);
            }

            @Override
            public synchronized void write(byte[] b, int off, int len) {
                try {
                    this.string.append(new String(b, 0, len, "UTF-8"));
                } catch (Exception e) {

                }
            }


            public String toString() {
                return this.string.toString();
            }
        };
    }

    public static OutputStream writeInputToOutputStream(InputStream in) throws IOException {
        OutputStream out = newOutputStream();
        byte[] buffer = new byte[1024];
        int len = in.read(buffer);
        while (len != -1) {
            out.write(buffer, 0, len);
            len = in.read(buffer);
        }
        out.close();
        return out;
    }

    public static OutputStream writeInputToOutputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len = in.read(buffer);
        while (len != -1) {
            out.write(buffer, 0, len);
            len = in.read(buffer);
        }
        out.close();
        return out;
    }

    public static <T, L extends List<T>> L typedList(List<?> untypedList, Class<T> itemClass, Class<L> listClass) {
        L list = null;
        try {
            list = listClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
        }
        for (Object item : untypedList) {
            list.add((T) item);
        }
        return list;
    }



}
