package io.github.winnpixie.btgui.utilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Function;

public class SystemHelper {
    public static final String CURRENT_DIRECTORY = System.getProperty("user.dir");
    public static final String HOME_DIRECTORY = System.getProperty("user.home");
    public static final Platform PLATFORM;

    static {
        PLATFORM = System.getProperty("os.name").contains("Windows")
                ? Platform.WINDOWS : Platform.UNIX;
    }

    public static String getDefaultJavaHome() {
        String home = System.getenv("JAVA_HOME");
        if (home == null || home.isEmpty()) return System.getProperty("java.home");

        return home;
    }

    public static String getDefaultMavenOptions() {
        String mavenOpts = System.getenv("MAVEN_OPTS");
        if (mavenOpts == null || mavenOpts.isEmpty()) return "-Xms1024M -Xmx1024M";

        return mavenOpts;
    }

    public static void openFolder(File file) {
        if (!file.isDirectory()) return; // TODO: Is this... ever an issue?

        try {
            Desktop.getDesktop().open(file);
            return;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        openLink(file.toURI());
    }

    public static void openLink(URL url) {
        try {
            openLink(url.toURI());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    public static void openLink(URI uri) {
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public enum Platform {
        WINDOWS(path -> path.indexOf(' ') < 0 ? path : String.format("\"%s\"", path)),
        UNIX(path -> path.replace(" ", "\\ "));

        private final Function<String, String> pathFormatter;

        Platform(Function<String, String> pathFormatter) {
            this.pathFormatter = pathFormatter;
        }

        public Function<String, String> getPathFormatter() {
            return pathFormatter;
        }
    }
}
