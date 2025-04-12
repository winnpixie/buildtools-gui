package io.github.winnpixie.btgui.options;

import io.github.winnpixie.btgui.utilities.Extensions;
import io.github.winnpixie.btgui.utilities.SystemHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProgramOptions {
    public static final ProgramOptions TEMPLATE = new ProgramOptions();

    public boolean downloadBuildTools = true;
    public boolean isolateRuns = true;
    public boolean openOutputAfterFinish = true;
    public boolean deleteWorkDirOnFinish = false;

    public String javaHome = SystemHelper.getDefaultJavaHome();
    public String jvmArguments = "-Xms1024M -Xmx1024M";

    public String mavenOptions = SystemHelper.getDefaultMavenOptions();

    private ProgramOptions() {
        /* internal use only */
    }

    public ProgramOptions(ProgramOptions template) {
        this.downloadBuildTools = template.downloadBuildTools;
        this.isolateRuns = template.isolateRuns;
        this.openOutputAfterFinish = template.openOutputAfterFinish;
        this.deleteWorkDirOnFinish = template.deleteWorkDirOnFinish;
        this.javaHome = template.javaHome;
        this.jvmArguments = template.jvmArguments;
        this.mavenOptions = template.mavenOptions;
    }

    public List<String> buildCommand() {
        List<String> command = new ArrayList<>();

        String javaPath = SystemHelper.PLATFORM.getPathFormatter()
                .apply(String.format("%s%cbin%<cjava", javaHome, File.separatorChar));
        command.add(javaPath);

        Extensions.addAll(command, jvmArguments.split(" "));
        Extensions.addAll(command, "-jar", "BuildTools.jar");

        return command;
    }
}
