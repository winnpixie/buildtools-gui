package io.github.winnpixie.btgui.utilities;

import io.github.winnpixie.btgui.BuildToolsGUI;

import java.io.File;

/* BuildTools' options, derived from
https://hub.spigotmc.org/stash/projects/SPIGOT/repos/buildtools/browse/src/main/java/org/spigotmc/builder/Builder.java
 */
public class BTOptions {
    public static boolean skipCertCheck = false; // disable-certificate-check
    public static boolean skipJavaVersionCheck = false; // disable-java-check
    public static boolean skipGitPull = false; // dont-update
    public static boolean generateSourcesJar = false; // generate-source
    public static boolean generateJavadocsJar = false; // generate-docs
    public static boolean developerMode = false; // dev
    public static boolean remapped = false; // remapped
    public static boolean compileIfChanged = false; // compile-if-changed

    public static boolean compileSpigot = true;
    public static boolean compileCraftBukkit = false;
    public static boolean compileNothing = false;

    public static String outputDirectory = BuildToolsGUI.CURRENT_DIRECTORY.getAbsolutePath(); // output-dir/o <DIRECTORY>
    public static String revision = "latest"; // rev <REVISION>
    public static String pullRequests = ""; // pull-request/pr <REPO:ID>

    public static String buildCommand(String javaPath, String btPath) {
        StringBuilder builder = new StringBuilder(javaPath);
        if (!javaPath.endsWith(File.separator)) builder.append(File.separatorChar);
        builder.append("bin").append(File.separatorChar).append("java -jar ");
        builder.append(btPath).append(' ');

        if (skipCertCheck) builder.append("--disable-certificate-check ");
        if (skipJavaVersionCheck) builder.append("--disable-java-check ");
        if (skipGitPull) builder.append("--dont-update ");
        if (generateSourcesJar) builder.append("--generate-source ");
        if (generateJavadocsJar) builder.append("--generate-docs ");
        if (developerMode) builder.append("--dev ");
        if (remapped) builder.append("--remapped ");
        if (compileIfChanged) builder.append("--compile-if-changed ");

        if (!skipGitPull && !developerMode) {
            builder.append("--rev ").append(revision).append(' ');
        }

        if (!pullRequests.isEmpty()) {
            for (String pr : pullRequests.split(",")) {
                String[] data = pr.split(":", 2);
                if (data.length < 2) continue;

                builder.append("--pull-request ").append(pr).append(' ');
            }
        }

        if (compileNothing) {
            builder.append("--compile NONE ");
        } else if (compileSpigot && compileCraftBukkit) {
            builder.append("--compile CRAFTBUKKIT,SPIGOT ");
        } else if (compileCraftBukkit) {
            builder.append("--compile CRAFTBUKKIT ");
        } else if (compileSpigot) {
            builder.append("--compile SPIGOT ");
        }

        return builder.toString();
    }
}
