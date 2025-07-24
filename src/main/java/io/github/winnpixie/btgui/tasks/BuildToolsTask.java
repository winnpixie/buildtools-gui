package io.github.winnpixie.btgui.tasks;

import io.github.winnpixie.btgui.configurations.BuildToolsConfiguration;
import io.github.winnpixie.btgui.configurations.JavaConfiguration;
import io.github.winnpixie.btgui.configurations.ProgramConfiguration;
import io.github.winnpixie.btgui.utilities.IOHelper;
import io.github.winnpixie.btgui.utilities.SystemHelper;
import io.github.winnpixie.logging.LogLevel;
import io.github.winnpixie.logging.PipedLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.IntSupplier;

public class BuildToolsTask implements IntSupplier {
    private final ProgramConfiguration programConfiguration;
    private final JavaConfiguration javaConfiguration;
    private final BuildToolsConfiguration buildToolsConfiguration;

    private final PipedLogger logger;

    public BuildToolsTask(PipedLogger logger,
                          ProgramConfiguration programConfiguration,
                          JavaConfiguration javaConfiguration,
                          BuildToolsConfiguration buildToolsConfiguration) {
        this.logger = logger;
        this.programConfiguration = programConfiguration;
        this.javaConfiguration = javaConfiguration;
        this.buildToolsConfiguration = buildToolsConfiguration;
    }

    @Override
    public int getAsInt() {
        List<String> javaCommand = javaConfiguration.toList();
        List<String> buildToolsArguments = buildToolsConfiguration.toList();
        logger.info("JAVA COMMAND = %s", String.join(" ", javaCommand));
        logger.info("PROGRAM ARGUMENTS = %s", String.join(" ", buildToolsArguments));

        logger.info("Creating working directory");
        Path workingPath = createWorkPath();
        if (workingPath == null) return -1337;

        Path buildToolsPath = Paths.get(SystemHelper.CURRENT_DIRECTORY, "BuildTools.jar");
        if (programConfiguration.downloadBuildTools()) {
            logger.info("Downloading BuildTools.jar from https://hub.spigotmc.org/");
            byte[] buildToolsData = downloadBuildTools();
            if (buildToolsData.length == 0) return -420;
            if (!saveBuildTools(buildToolsPath, buildToolsData)) return -420;
        }

        logger.info("Copying BuildTools.jar to run directory");
        if (!copyBuildToolsToWorkingPath(buildToolsPath, workingPath)) return -69;

        logger.info("Full Command:");
        ProcessBuilder buildToolsProcessBuilder = buildProcess(workingPath);
        logger.info(String.join(" ", buildToolsProcessBuilder.command()));

        logger.info("Running BuildTools...");
        return runBuildTools(buildToolsProcessBuilder, workingPath);
    }

    private byte[] downloadBuildTools() {
        try {
            return IOHelper.getBytes("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar");
        } catch (IOException exception) {
            logger.log(LogLevel.SEVERE, exception, "Error downloading BuildTools");
            return new byte[0];
        }
    }

    private boolean saveBuildTools(Path buildToolsPath, byte[] data) {
        try {
            Files.write(buildToolsPath, data);
            return true;
        } catch (IOException exception) {
            logger.log(LogLevel.SEVERE, exception, "Error saving BuildTools");
            return false;
        }
    }

    private Path createWorkPath() {
        Path runPath = Paths.get(SystemHelper.CURRENT_DIRECTORY, !programConfiguration.isolateProcesses() ? "BuildTools"
                : String.format("BuildTools-%d", System.currentTimeMillis()));

        if (Files.notExists(runPath)) {
            try {
                Files.createDirectory(runPath);
            } catch (IOException exception) {
                logger.log(LogLevel.SEVERE, exception, "Error creating work directory");
                return null;
            }
        }

        logger.info("Created run directory @ %s", runPath);
        return runPath;
    }

    private boolean copyBuildToolsToWorkingPath(Path buildToolsPath, Path runPath) {
        try {
            Files.copy(buildToolsPath, runPath.resolve("BuildTools.jar"), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException exception) {
            logger.log(LogLevel.SEVERE, exception, "Error copying BuildTools to work directory");
            return false;
        }
    }

    private ProcessBuilder buildProcess(Path workingPath) {
        ProcessBuilder builder = new ProcessBuilder()
                .command(javaConfiguration.toList())
                .directory(workingPath.toFile())
                .redirectErrorStream(true);
        builder.command().addAll(buildToolsConfiguration.toList());

        builder.environment().put("JAVA_HOME", javaConfiguration.javaHome());

        builder.environment().put("MAVEN_OPTS", javaConfiguration.mavenArguments());

        return builder;
    }

    private int runBuildTools(ProcessBuilder buildToolsProcessBuilder, Path workingDir) {
        long startTime = System.currentTimeMillis();

        try {
            Process buildToolsProcess = buildToolsProcessBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(buildToolsProcess.getInputStream()))) {
                reader.lines().forEach(line -> logger.info("[BT] %s", line));
            }

            while (buildToolsProcess.isAlive()) {
                buildToolsProcess.destroy();
            }

            int exitCode = buildToolsProcess.exitValue();
            if (exitCode == 0 && programConfiguration.showOutputOnFinish()) {
                File outputDir = buildToolsConfiguration.outputDirectory().isEmpty() ? workingDir.toFile()
                        : new File(buildToolsConfiguration.outputDirectory());
                logger.info("Opening '%s' in system file explorer", outputDir.getPath());
                SystemHelper.openFolder(outputDir);
            }

            return exitCode;
        } catch (Exception exception) {
            logger.log(LogLevel.SEVERE, exception, "Error running BuildTools");
            return -42;
        } finally {
            logger.info("BuildsTools took %.3fs", (System.currentTimeMillis() - startTime) / 1000.0);

            cleanUp(workingDir);
        }
    }

    // (Optionally) Clean up after ourselves. :)
    private void cleanUp(Path runPath) {
        // FIXME: Something (possibly PortableGit?) causes this to fail.
        if (programConfiguration.deleteWorkOnFinish()) {
            logger.info("Deleting work directory...");

            try {
                IOHelper.deleteRecursively(runPath);
                logger.info("Deleted work directory.");
            } catch (IOException exception) {
                logger.log(LogLevel.WARNING, exception, "Issue deleting work directory");
            }
        }
    }
}
