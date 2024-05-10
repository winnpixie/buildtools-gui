package io.github.winnpixie.btgui.tasks;

import io.github.winnpixie.btgui.BuildToolsGUI;
import io.github.winnpixie.btgui.config.BuildToolsOptions;
import io.github.winnpixie.btgui.config.ProgramOptions;
import io.github.winnpixie.btgui.utilities.IOHelper;
import io.github.winnpixie.btgui.utilities.OSHelper;
import io.github.winnpixie.logging.CustomLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class BuildToolsExecutor {
    private final List<String> javaCommand;
    private final List<String> buildToolsArguments;
    private final CustomLogger logger = new CustomLogger();

    private boolean active;

    public BuildToolsExecutor(List<String> javaCommand, List<String> buildToolsArguments) {
        this.javaCommand = javaCommand;
        this.buildToolsArguments = buildToolsArguments;
    }

    public List<String> getJavaCommand() {
        return javaCommand;
    }

    public List<String> getBuildToolsArguments() {
        return buildToolsArguments;
    }

    public CustomLogger getLogger() {
        return logger;
    }

    public boolean isActive() {
        return active;
    }

    public boolean run() {
        active = true;

        File buildToolsFile = new File(BuildToolsGUI.CURRENT_DIRECTORY, "BuildTools.jar");
        if (ProgramOptions.downloadBuildTools) {
            logger.info("Downloading BuildTools.jar from https://hub.spigotmc.org/");

            byte[] buildToolsData = downloadBuildTools();
            if (buildToolsData.length == 0) {
                logger.error("Could not download BuildTools, stopping!");

                active = false;
                return false;
            }

            if (!saveBuildTools(buildToolsFile, buildToolsData)) {
                logger.error("Could not save BuildTools.jar to storage device, stopping!");

                active = false;
                return false;
            }
        }

        logger.info("Creating run directory");
        File runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, "run");
        if (ProgramOptions.isolateRuns) {
            runDir = new File(BuildToolsGUI.CURRENT_DIRECTORY, String.format("run-%d", System.currentTimeMillis()));
        }

        if (!runDir.exists() && !runDir.mkdir()) {
            logger.error("Could not create run directory, stopping!");

            active = false;
            return false;
        }

        logger.info(String.format("Created run directory @ %s", runDir.getPath()));

        logger.info("Copying BuildTools.jar to run directory");
        if (!copyBuildToolsToRunDirectory(buildToolsFile, runDir)) {
            logger.error("Could not copy BuildTools.jar to run directory!");

            active = false;
            return false;
        }

        logger.info("Java Command:");
        logger.info(String.join(" ", javaCommand));

        logger.info("BuildTools Arguments:");
        logger.info(String.join(" ", buildToolsArguments));

        logger.info("Full Command:");
        ProcessBuilder buildToolsProcessBuilder = buildProcess(javaCommand, runDir, buildToolsArguments);
        logger.info(String.join(" ", buildToolsProcessBuilder.command()));

        logger.info("Running BuildTools, please wait...");
        try {
            long startTime = System.nanoTime();
            Process buildToolsProcess = buildToolsProcessBuilder.start();

            while (buildToolsProcess.isAlive()) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(buildToolsProcess.getInputStream()))) {
                    br.lines().forEach(logger::info);
                }
            }

            logger.info(String.format("\nBuildTools took roughly %.3fs to complete.",
                    (System.nanoTime() - startTime) / 1_000_000_000.0));

            if (ProgramOptions.openOutputAfterFinish) {
                logger.info(String.format("\nOpening %s in system file explorer...", BuildToolsOptions.outputDirectory));
                OSHelper.showDirectory(new File(BuildToolsOptions.outputDirectory));
            }

            return true;
        } catch (IOException e) {
            logger.error(e);
            return false;
        } finally {
            // (optionally) Clean up after ourselves :)
            if (ProgramOptions.deleteWorkDirOnFinish) {
                logger.info("Deleting work directory...");

                if (deleteRunDirectory(runDir)) {
                    logger.info("Deleted work directory.");
                } else {
                    logger.warn("Could not delete work directory.");
                }
            }

            active = false;
        }
    }

    private byte[] downloadBuildTools() {
        try {
            return IOHelper.getBytes("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar");
        } catch (IOException e) {
            logger.error(e);
            return new byte[0];
        }
    }

    private boolean saveBuildTools(File buildToolsFile, byte[] binData) {
        try {
            Files.write(buildToolsFile.toPath(), binData);
            return true;
        } catch (IOException e) {
            logger.error(e);
            return false;
        }
    }

    private boolean copyBuildToolsToRunDirectory(File originalBuildTools, File runDirectory) {
        try {
            File buildToolsCopy = new File(runDirectory, "BuildTools.jar");
            Files.copy(originalBuildTools.toPath(), buildToolsCopy.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            logger.error(e);
            return false;
        }
    }

    private ProcessBuilder buildProcess(List<String> javaCommand, File workingDirectory, List<String> buildToolsArguments) {
        ProcessBuilder builder = new ProcessBuilder()
                .command(javaCommand)
                .directory(workingDirectory)
                .redirectErrorStream(true);
        builder.command().addAll(buildToolsArguments);

        builder.environment().put("JAVA_HOME", ProgramOptions.javaHome);

        if (!ProgramOptions.mavenOptions.isEmpty()) {
            builder.environment().put("MAVEN_OPTS", ProgramOptions.mavenOptions);
        }

        return builder;
    }

    private boolean deleteRunDirectory(File directory) {
        try {
            IOHelper.delete(directory);
            return true;
        } catch (IOException e) {
            logger.error(e);
            return false;
        }
    }
}
