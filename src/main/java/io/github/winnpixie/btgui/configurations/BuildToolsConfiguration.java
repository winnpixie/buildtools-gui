package io.github.winnpixie.btgui.configurations;

import io.github.winnpixie.btgui.utilities.SystemHelper;

import java.util.*;

public class BuildToolsConfiguration {
    private final boolean disableCertificateCheck;
    private final boolean disableJavaCheck;
    private final boolean dontUpdate;

    private final Set<String> compilationTargets;

    private final String revision;
    private final Set<String> pullRequests;
    private final String outputDirectory;
    private final String finalName;

    public final boolean generateSources;
    public final boolean generateJavadocs;
    public final boolean developerMode;
    public final boolean experimental;
    public final boolean remapped;
    public final boolean compileIfChanged;

    private BuildToolsConfiguration(Builder builder) {
        this.disableCertificateCheck = builder.disableCertificateCheck;
        this.disableJavaCheck = builder.disableJavaCheck;
        this.dontUpdate = builder.dontUpdate;
        this.compilationTargets = builder.compilationTargets;
        this.revision = builder.revision;
        this.pullRequests = builder.pullRequests;
        this.outputDirectory = builder.outputDirectory;
        this.finalName = builder.finalName;
        this.generateSources = builder.generateSources;
        this.generateJavadocs = builder.generateJavadocs;
        this.developerMode = builder.developerMode;
        this.experimental = builder.experimental;
        this.remapped = builder.remapped;
        this.compileIfChanged = builder.compileIfChanged;
    }

    public boolean disableCertificateCheck() {
        return disableCertificateCheck;
    }

    public boolean disableJavaCheck() {
        return disableJavaCheck;
    }

    public boolean dontUpdate() {
        return dontUpdate;
    }

    public Set<String> compilationTargets() {
        return compilationTargets;
    }

    public String revision() {
        return revision;
    }

    public Set<String> pullRequests() {
        return pullRequests;
    }

    public String outputDirectory() {
        return outputDirectory;
    }

    public String finalName() {
        return finalName;
    }

    public boolean generateSources() {
        return generateSources;
    }

    public boolean generateJavadocs() {
        return generateJavadocs;
    }

    public boolean developerMode() {
        return developerMode;
    }

    public boolean experimental() {
        return experimental;
    }

    public boolean remapped() {
        return remapped;
    }

    public boolean compileIfChanged() {
        return compileIfChanged;
    }

    public List<String> toList() {
        List<String> arguments = new ArrayList<>();

        if (disableCertificateCheck) arguments.add("--disable-certificate-check");
        if (disableJavaCheck) arguments.add("--disable-java-check");
        if (dontUpdate) arguments.add("--dont-update");
        if (generateSources) arguments.add("--generate-source");
        if (generateJavadocs) arguments.add("--generate-docs");
        if (developerMode) arguments.add("--dev");
        if (experimental) arguments.add("--experimental");
        if (remapped) arguments.add("--remapped");
        if (compileIfChanged) arguments.add("--compile-if-changed");

        if (!dontUpdate && !developerMode && !experimental) {
            arguments.add("--rev");
            arguments.add(revision.isEmpty() ? "latest" : revision);
        }

        for (String pullRequest : pullRequests) {
            arguments.add("--pull-request");
            arguments.add(pullRequest);
        }

        if (compilationTargets.isEmpty() || compilationTargets.contains("NONE")) {
            arguments.add("--compile");
            arguments.add("NONE");
        } else {
            arguments.add("--compile");
            arguments.add(String.join(",", compilationTargets));
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

    public static class Builder {
        private boolean disableCertificateCheck = false;
        private boolean disableJavaCheck = false;
        private boolean dontUpdate = false;

        private final Set<String> compilationTargets = new HashSet<>(Collections.singleton("SPIGOT"));

        private String revision = "latest";
        private final Set<String> pullRequests = new HashSet<>();
        private String outputDirectory = "";
        private String finalName = "";

        private boolean generateSources = false;
        private boolean generateJavadocs = false;
        private boolean developerMode = false;
        private boolean experimental = false;
        private boolean remapped = false;
        private boolean compileIfChanged = false;

        public Builder disableCertificateCheck(boolean disable) {
            this.disableCertificateCheck = disable;
            return this;
        }

        public Builder disableJavaCheck(boolean disable) {
            this.disableJavaCheck = disable;
            return this;
        }

        public Builder dontUpdate(boolean skip) {
            this.dontUpdate = skip;
            return this;
        }

        public Builder addCompileTarget(String target) {
            this.compilationTargets.add(target);
            return this;
        }

        public Builder removeCompileTarget(String target) {
            this.compilationTargets.remove(target);
            return this;
        }

        public Builder revision(String revision) {
            this.revision = revision;
            return this;
        }

        public Builder pullRequests(String[] pullRequests) {
            this.pullRequests.clear();
            Collections.addAll(this.pullRequests, pullRequests);
            return this;
        }

        public Builder outputDirectory(String directory) {
            this.outputDirectory = directory;
            return this;
        }

        public Builder finalName(String name) {
            this.finalName = name;
            return this;
        }

        public Builder generateSources(boolean sources) {
            this.generateSources = sources;
            return this;
        }

        public Builder generateJavadocs(boolean javadocs) {
            this.generateJavadocs = javadocs;
            return this;
        }

        public Builder developerMode(boolean dev) {
            this.developerMode = dev;
            return this;
        }

        public Builder experimental(boolean experimental) {
            this.experimental = experimental;
            return this;
        }

        public Builder remapped(boolean remap) {
            this.remapped = remap;
            return this;
        }

        public Builder compileIfChanged(boolean compileIfChanged) {
            this.compileIfChanged = compileIfChanged;
            return this;
        }

        public BuildToolsConfiguration build() {
            return new BuildToolsConfiguration(this);
        }
    }
}
