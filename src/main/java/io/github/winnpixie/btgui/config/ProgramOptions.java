package io.github.winnpixie.btgui.config;

import io.github.winnpixie.btgui.utilities.OSHelper;

import java.io.File;

public class ProgramOptions {
    public static boolean downloadBuildTools = true;
    public static boolean isolateRuns = true;
    public static boolean deleteRunOnFinish = true;

    public static String javaHome = getDefaultJavaHome();
    public static String heapSize = "1024M";

    private static String getDefaultJavaHome() {
        String home = System.getenv("JAVA_HOME");
        if (home == null) return System.getProperty("java.home");

        return home;
    }

    public static String[] buildJavaCommand() {
        String javaPath = OSHelper.getPlatform().getPathFormatter()
                .apply(String.format("%s%cbin%cjava", ProgramOptions.javaHome, File.separatorChar, File.separatorChar));

        return new String[]{
                javaPath,
                String.format("-Xms%s", ProgramOptions.heapSize),
                String.format("-Xmx%s", ProgramOptions.heapSize),
                "-jar",
                "BuildTools.jar"
        };
    }
}
