package io.github.winnpixie.btgui.config;

import io.github.winnpixie.btgui.utilities.Extensions;
import io.github.winnpixie.btgui.utilities.OSHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProgramOptions {
    public static boolean downloadBuildTools = true;
    public static boolean isolateRuns = true;
    public static boolean openOutputAfterFinish = true;
    public static boolean deleteWorkDirOnFinish = false;

    public static String javaHome = getDefaultJavaHome();
    public static String jvmArguments = "-Xms512M -Xmx1024M";

    public static String mavenOptions = getDefaultMavenOptions();

    private static String getDefaultJavaHome() {
        String home = System.getenv("JAVA_HOME");
        if (home == null || home.isEmpty()) return System.getProperty("java.home");

        return home;
    }

    private static String getDefaultMavenOptions() {
        String mavenOpts = System.getenv("MAVEN_OPTS");
        if (mavenOpts == null || mavenOpts.isEmpty()) return "-Xms1024M -Xmx1024M";

        return mavenOpts;
    }

    public static List<String> buildJavaCommand() {
        List<String> command = new ArrayList<>();

        String javaPath = OSHelper.getPlatform().getPathFormatter()
                .apply(String.format("%s%cbin%<cjava", ProgramOptions.javaHome, File.separatorChar));
        command.add(javaPath);

        Extensions.addAll(command, jvmArguments.split(" "));
        Extensions.addAll(command, "-jar", "BuildTools.jar");

        return command;
    }
}
