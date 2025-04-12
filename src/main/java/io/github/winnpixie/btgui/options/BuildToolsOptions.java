package io.github.winnpixie.btgui.options;

import io.github.winnpixie.btgui.utilities.SystemHelper;

import java.util.ArrayList;
import java.util.List;

/* BuildTools' options, derived from
 * https://hub.spigotmc.org/stash/projects/SPIGOT/repos/buildtools/browse/src/main/java/org/spigotmc/builder/Builder.java
 */
public class BuildToolsOptions {
    public static final BuildToolsOptions TEMPLATE = new BuildToolsOptions();

    public boolean skipCertCheck = false; // disable-certificate-check
    public boolean skipJavaVersionCheck = false; // disable-java-check
    public boolean skipGitPull = false; // dont-update

    /* deprecated, use --compile NONE */
    private boolean skipCompile = false; // skip-compile

    public boolean generateSourcesJar = false; // generate-source
    public boolean generateJavadocsJar = false; // generate-docs
    public boolean developerMode = false; // dev
    public boolean experimentalMode = false; // experimental
    public boolean remapped = false; // remapped
    public boolean compileIfChanged = false; // compile-if-changed

    public boolean compileSpigot = true;
    public boolean compileCraftBukkit = false;
    public boolean compileNothing = false;

    public String revision = "latest"; // rev <VERSION>
    public String pullRequests = ""; // pull-request/pr <REPO:ID>
    public String outputDirectory = ""; // output-dir/o <PATH>
    public String finalName = ""; // final-name <NAME>

    private BuildToolsOptions() {
        /* internal use only */
    }

    public BuildToolsOptions(BuildToolsOptions template) {
        this.skipCertCheck = template.skipCertCheck;
        this.skipJavaVersionCheck = template.skipJavaVersionCheck;
        this.skipGitPull = template.skipGitPull;
        this.skipCompile = template.skipCompile;
        this.generateSourcesJar = template.generateSourcesJar;
        this.generateJavadocsJar = template.generateJavadocsJar;
        this.developerMode = template.developerMode;
        this.experimentalMode = template.experimentalMode;
        this.remapped = template.remapped;
        this.compileIfChanged = template.compileIfChanged;
        this.compileSpigot = template.compileSpigot;
        this.compileCraftBukkit = template.compileCraftBukkit;
        this.compileNothing = template.compileNothing;
        this.revision = template.revision;
        this.pullRequests = template.pullRequests;
        this.outputDirectory = template.outputDirectory;
        this.finalName = template.finalName;
    }

    public List<String> buildArguments() {
        List<String> arguments = new ArrayList<>();

        if (skipCertCheck) arguments.add("--disable-certificate-check");
        if (skipJavaVersionCheck) arguments.add("--disable-java-check");
        if (skipGitPull) arguments.add("--dont-update");
        if (skipCompile) arguments.add("--skip-compile");
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

        if (!finalName.isEmpty()) {
            arguments.add("--final-name");
            arguments.add(finalName);
        }

        if (!outputDirectory.isEmpty()) {
            arguments.add("--output-dir");
            arguments.add(SystemHelper.PLATFORM.getPathFormatter().apply(outputDirectory));
        }

        return arguments;
    }
}
