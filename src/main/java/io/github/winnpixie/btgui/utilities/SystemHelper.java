package io.github.winnpixie.btgui.utilities;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.logging.LogLevel;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.function.UnaryOperator;

public final class SystemHelper {
    public static final String CURRENT_DIRECTORY = System.getProperty("user.dir");
    public static final String HOME_DIRECTORY = System.getProperty("user.home");
    public static final Platform PLATFORM;

    static {
        PLATFORM = System.getProperty("os.name").contains("Windows")
                ? Platform.WINDOWS : Platform.UNIX;
    }

    private SystemHelper() {
    }

    public static String getDefaultJavaHome() {
        String home = System.getenv("JAVA_HOME");
        if (home == null || home.isEmpty()) return System.getProperty("java.home");

        return home;
    }

    public static String getJavaExecutable(String javaHome) {
        StringBuilder pathBuilder = new StringBuilder(javaHome)
                .append(File.separatorChar).append("bin")
                .append(File.separatorChar).append("java");
        if (PLATFORM == Platform.WINDOWS) pathBuilder.append(".exe");

        return PLATFORM.getPathFormatter().apply(pathBuilder.toString());
    }

    public static String getDefaultMavenOptions() {
        String mavenOpts = System.getenv("MAVEN_OPTS");
        if (mavenOpts == null || mavenOpts.isEmpty()) return "-Xms1024M -Xmx1024M";

        return mavenOpts;
    }

    public static void openFolder(File file) {
        if (!file.isDirectory()) return;

        try {
            Desktop.getDesktop().open(file);
            return;
        } catch (Exception exception) {
            BuildToolsGUI.LOGGER.log(LogLevel.WARNING, exception, "Issue opening folder");
        }

        openLink(file.toURI());
    }

    public static void openLink(URL url) {
        try {
            openLink(url.toURI());
        } catch (Exception exception) {
            BuildToolsGUI.LOGGER.log(LogLevel.WARNING, exception, "Issue opening link");
        }
    }

    public static void openLink(URI uri) {
        try {
            Desktop.getDesktop().browse(uri);
        } catch (Exception exception) {
            BuildToolsGUI.LOGGER.log(LogLevel.WARNING, exception, "Issue opening link");
        }
    }

    public enum Platform {
        WINDOWS(path -> path.indexOf(' ') < 0 ? path : String.format("\"%s\"", path)),
        UNIX(path -> path.replace(" ", "\\ "));

        private final UnaryOperator<String> pathFormatter;

        Platform(UnaryOperator<String> pathFormatter) {
            this.pathFormatter = pathFormatter;
        }

        public UnaryOperator<String> getPathFormatter() {
            return pathFormatter;
        }
    }
}
