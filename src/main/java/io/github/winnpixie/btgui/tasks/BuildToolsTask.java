package io.github.winnpixie.btgui.tasks;

import io.github.winnpixie.btgui.config.BuildToolsOptions;
import io.github.winnpixie.btgui.config.ProgramOptions;
import io.github.winnpixie.btgui.utilities.IOHelper;
import io.github.winnpixie.btgui.utilities.SystemHelper;
import io.github.winnpixie.logging.CustomLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Supplier;

public class BuildToolsTask implements Supplier<Boolean> {
    private final List<String> javaCommand;
    private final List<String> programArguments;

    private CustomLogger logger;

    public BuildToolsTask(List<String> javaCommand, List<String> programArguments) {
        this.javaCommand = javaCommand;
        this.programArguments = programArguments;
    }

    public void setLogger(CustomLogger logger) {
        this.logger = logger;
    }

    @Override
    public Boolean get() {
        logger.info(String.format("JAVA COMMAND = %s", String.join(" ", javaCommand)));
        logger.info(String.format("PROGRAM ARGUMENTS = %s", String.join(" ", programArguments)));

        File buildToolsFile = new File(SystemHelper.CURRENT_DIRECTORY, "BuildTools.jar");
        if (ProgramOptions.downloadBuildTools) {
            logger.info("Downloading BuildTools.jar from https://hub.spigotmc.org/");

            byte[] buildToolsData = downloadBuildTools();
            if (buildToolsData.length == 0) return false;
            if (!saveBuildTools(buildToolsFile, buildToolsData)) return false;
        }

        logger.info("Creating run directory");
        File runDir = new File(SystemHelper.CURRENT_DIRECTORY, "run");
        if (ProgramOptions.isolateRuns) {
            runDir = new File(SystemHelper.CURRENT_DIRECTORY, String.format("run-%d", System.currentTimeMillis()));
        }

        if (!runDir.exists() && !runDir.mkdir()) return false;

        logger.info(String.format("Created run directory @ %s", runDir.getPath()));

        logger.info("Copying BuildTools.jar to run directory");
        if (!copyBuildToolsToRunDirectory(buildToolsFile, runDir)) return false;

        logger.info("Java Command:");
        logger.info(String.join(" ", javaCommand));

        logger.info("BuildTools Arguments:");
        logger.info(String.join(" ", programArguments));

        logger.info("Full Command:");
        ProcessBuilder buildToolsProcessBuilder = buildProcess(javaCommand, runDir, programArguments);
        logger.info(String.join(" ", buildToolsProcessBuilder.command()));

        logger.info("Running BuildTools...");
        try {
            long startTime = System.nanoTime();
            Process buildToolsProcess = buildToolsProcessBuilder.start();

            while (buildToolsProcess.isAlive()) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(buildToolsProcess.getInputStream()))) {
                    br.lines().forEach(logger::print);
                }
            }

            logger.info(String.format("\nBuildTools took roughly %.3fs to complete.",
                    (System.nanoTime() - startTime) / 1_000_000_000.0));

            if (ProgramOptions.openOutputAfterFinish) {
                logger.info(String.format("\nOpening %s in system file explorer...", BuildToolsOptions.outputDirectory));
                SystemHelper.openFolder(new File(BuildToolsOptions.outputDirectory));
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
            IOHelper.deleteRecursively(directory);
            return true;
        } catch (IOException e) {
            logger.error(e);
            return false;
        }
    }
}
