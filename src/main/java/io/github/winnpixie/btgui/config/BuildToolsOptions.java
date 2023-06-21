package io.github.winnpixie.btgui.config;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.utilities.OSHelper;

import java.util.ArrayList;
import java.util.List;

/* BuildTools' options, derived from
https://hub.spigotmc.org/stash/projects/SPIGOT/repos/buildtools/browse/src/main/java/org/spigotmc/builder/Builder.java
 */
public class BuildToolsOptions {
    public static boolean skipCertCheck = false; // disable-certificate-check
    public static boolean skipJavaVersionCheck = false; // disable-java-check
    public static boolean skipGitPull = false; // dont-update
    public static boolean generateSourcesJar = false; // generate-source
    public static boolean generateJavadocsJar = false; // generate-docs
    public static boolean developerMode = false; // dev
    public static boolean experimentalMode = false; // experimental
    public static boolean remapped = false; // remapped
    public static boolean compileIfChanged = false; // compile-if-changed

    public static boolean compileSpigot = true;
    public static boolean compileCraftBukkit = false;
    public static boolean compileNothing = false;

    public static String outputDirectory = BuildToolsGUI.CURRENT_DIRECTORY.getAbsolutePath(); // output-dir/o <DIRECTORY>
    public static String revision = "latest"; // rev <REVISION>
    public static String pullRequests = ""; // pull-request/pr <REPO:ID>

    public static List<String> buildArguments() {
        List<String> arguments = new ArrayList<>();

        if (skipCertCheck) arguments.add("--disable-certificate-check");
        if (skipJavaVersionCheck) arguments.add("--disable-java-check");
        if (skipGitPull) arguments.add("--dont-update");
        if (generateSourcesJar) arguments.add("--generate-source");
        if (generateJavadocsJar) arguments.add("--generate-docs");
        if (developerMode) arguments.add("--dev");
        if (experimentalMode) arguments.add("--experimental");
        if (remapped) arguments.add("--remapped");
        if (compileIfChanged) arguments.add("--compile-if-changed");

        if (!skipGitPull && !developerMode && !experimentalMode) {
            arguments.add("--rev");
            arguments.add(revision);
        }

        if (!pullRequests.isEmpty()) {
            for (String pr : pullRequests.split(",")) {
                String[] data = pr.split(":", 2);
                if (data.length < 2) continue;

                arguments.add("--pull-request");
                arguments.add(pr);
            }
        }

        if (compileNothing) {
            arguments.add("--compile");
            arguments.add("NONE");
        } else if (compileSpigot && compileCraftBukkit) {
            arguments.add("--compile");
            arguments.add("CRAFTBUKKIT,SPIGOT");
        } else if (compileCraftBukkit) {
            arguments.add("--compile");
            arguments.add("CRAFTBUKKIT");
        } else if (compileSpigot) {
            arguments.add("--compile");
            arguments.add("SPIGOT");
        }

        arguments.add("--output-dir");
        arguments.add(OSHelper.getPlatform().getPathFormatter().apply(outputDirectory));

        return arguments;
    }
}
