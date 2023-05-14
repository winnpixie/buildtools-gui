package io.github.winnpixie.btgui.config;

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
}
