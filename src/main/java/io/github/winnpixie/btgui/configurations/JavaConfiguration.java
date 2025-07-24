package io.github.winnpixie.btgui.configurations;

import io.github.winnpixie.btgui.utilities.SystemHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaConfiguration {
    private final String javaHome;
    private final String javaArguments;

    private final String mavenArguments;

    private JavaConfiguration(Builder builder) {
        this.javaHome = builder.javaHome;
        this.javaArguments = builder.javaArguments;
        this.mavenArguments = builder.mavenArguments;
    }

    public String javaHome() {
        return javaHome;
    }

    public String javaArguments() {
        return javaArguments;
    }

    public String mavenArguments() {
        return mavenArguments;
    }

    public List<String> toList() {
        List<String> list = new ArrayList<>();
        list.add(SystemHelper.getJavaExecutable(javaHome));

        Collections.addAll(list, javaArguments.split(" "));
        Collections.addAll(list, "-jar", "BuildTools.jar");

        return list;
    }

    public static class Builder {
        private String javaHome = "";
        private String javaArguments = "-Xmx1024M -Xms1024M";
        private String mavenArguments = SystemHelper.getDefaultMavenOptions();

        public Builder javaHome(String path) {
            this.javaHome = path;
            return this;
        }

        public Builder javaArguments(String arguments) {
            this.javaArguments = arguments;
            return this;
        }

        public Builder mavenArguments(String arguments) {
            this.mavenArguments = arguments;
            return this;
        }

        public JavaConfiguration build() {
            if (javaHome.isEmpty()) javaHome = SystemHelper.getDefaultJavaHome();

            return new JavaConfiguration(this);
        }
    }
}
