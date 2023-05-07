package io.github.winnpixie.btgui.utilities;

/* BuildTools' options, derived from
https://hub.spigotmc.org/stash/projects/SPIGOT/repos/buildtools/browse/src/main/java/org/spigotmc/builder/Builder.java
 */
public class BTOptions {
    public static boolean skipCertCheck = false; // disable-certificate-check
    public static boolean skipJavaVersionCheck = false; // disable-java-check
    public static boolean skipGitPull = false; // dont-update
    public static boolean dontCompile = false; // skip-compile
    public static boolean generateSourcesJar = false; // generate-source
    public static boolean generateJavadocsJar = false; // generate-docs
    public static boolean developerMode = false; // dev
    public static boolean remapped = false; // remapped
    public static boolean compileIfChanged = false; // compile-if-changed

    public static String softwareToCompile = "SPIGOT"; // compile <SOFTWARE>
    public static String outputDirectory = "."; // output-dir/o <DIRECTORY>
    public static String revision = "latest"; // rev <REVISION>
    public static String pullRequest = ""; // pull-request/pr <REPO:ID>
}
