package io.github.winnpixie.btgui.tasks;

import io.github.winnpixie.btgui.options.BuildToolsOptions;
import io.github.winnpixie.btgui.options.ProgramOptions;
import io.github.winnpixie.btgui.utilities.IOHelper;
import io.github.winnpixie.btgui.utilities.SystemHelper;
import io.github.winnpixie.logging.CustomLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Supplier;

public class BuildToolsTask implements Supplier<Boolean> {
    private final ProgramOptions programOptions;
    private final BuildToolsOptions buildToolsOptions;

    private CustomLogger logger;

    public BuildToolsTask(ProgramOptions programOptions, BuildToolsOptions buildToolsOptions) {
        this.programOptions = programOptions;
        this.buildToolsOptions = buildToolsOptions;
    }

    public void setLogger(CustomLogger logger) {
        this.logger = logger;
    }

    @Override
    public Boolean get() {
        List<String> javaCommand = programOptions.buildCommand();
        List<String> buildToolsArguments = buildToolsOptions.buildArguments();
        logger.info(String.format("JAVA COMMAND = %s", String.join(" ", javaCommand)));
        logger.info(String.format("PROGRAM ARGUMENTS = %s", String.join(" ", buildToolsArguments)));

        logger.info("Creating working directory");
        Path workingDir = createWorkingDirectory();
        if (workingDir == null) return false;

        Path buildToolsPath = Paths.get(SystemHelper.CURRENT_DIRECTORY, "BuildTools.jar");
        if (programOptions.downloadBuildTools) {
            logger.info("Downloading BuildTools.jar from https://hub.spigotmc.org/");
            byte[] buildToolsData = downloadBuildTools();
            if (buildToolsData.length == 0) return false;
            if (!saveBuildTools(buildToolsPath, buildToolsData)) return false;
        }

        logger.info("Copying BuildTools.jar to run directory");
        if (!copyBuildToolsToRunDirectory(buildToolsPath, workingDir)) return false;

        logger.info("Full Command:");
        ProcessBuilder buildToolsProcessBuilder = buildProcess(javaCommand, workingDir.toFile(), buildToolsArguments);
        logger.info(String.join(" ", buildToolsProcessBuilder.command()));

        logger.info("Running BuildTools...");
        return runBuildTools(buildToolsProcessBuilder, workingDir);
    }

    private byte[] downloadBuildTools() {
        try {
            return IOHelper.getBytes("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar");
        } catch (IOException e) {
            logger.error(e);

            return new byte[0];
        }
    }

    private boolean saveBuildTools(Path buildToolsPath, byte[] data) {
        try {
            Files.write(buildToolsPath, data);

            return true;
        } catch (IOException e) {
            logger.error(e);

            return false;
        }
    }

    private Path createWorkingDirectory() {
        Path runPath = Paths.get(SystemHelper.CURRENT_DIRECTORY, "run");
        if (programOptions.isolateRuns) {
            runPath = Paths.get(SystemHelper.CURRENT_DIRECTORY, String.format("run-%d", System.currentTimeMillis()));
        }

        if (Files.notExists(runPath)) {
            try {
                Files.createDirectory(runPath);
            } catch (IOException e) {
                logger.error(e);

                return null;
            }
        }

        logger.info(String.format("Created run directory @ %s", runPath));
        return runPath;
    }

    private boolean copyBuildToolsToRunDirectory(Path originalBuildTools, Path runPath) {
        try {
            Path buildToolsCopy = runPath.resolve("BuildTools.jar");
            Files.copy(originalBuildTools, buildToolsCopy, StandardCopyOption.REPLACE_EXISTING);

            return true;
        } catch (IOException e) {
            logger.error(e);

            return false;
        }
    }

    private ProcessBuilder buildProcess(List<String> javaCommand, File workingDir, List<String> buildToolsArguments) {
        ProcessBuilder builder = new ProcessBuilder()
                .command(javaCommand)
                .directory(workingDir)
                .redirectErrorStream(true);
        builder.command().addAll(buildToolsArguments);

        builder.environment().put("JAVA_HOME", programOptions.javaHome);

        if (!programOptions.mavenOptions.isEmpty()) {
            builder.environment().put("MAVEN_OPTS", programOptions.mavenOptions);
        }

        return builder;
    }

    private boolean runBuildTools(ProcessBuilder buildToolsProcessBuilder, Path workingDir) {
        long startTime = System.currentTimeMillis();

        try {
            Process buildToolsProcess = buildToolsProcessBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(buildToolsProcess.getInputStream()))) {
                reader.lines().forEach(logger::print);
            }

            buildToolsProcess.destroy();
            return true;
        } catch (IOException e) {
            logger.error(e);

            return false;
        } finally {
            logger.info(String.format("\nBuildTools took approximately %.3fs to complete.",
                    (System.currentTimeMillis() - startTime) / 1_000.0));

            if (programOptions.openOutputAfterFinish) {
                File outputDir = buildToolsOptions.outputDirectory.isEmpty() ? workingDir.toFile()
                        : new File(buildToolsOptions.outputDirectory);
                logger.info(String.format("\nOpening %s in system file explorer", outputDir.getPath()));
                SystemHelper.openFolder(outputDir);
            }

            cleanUp(workingDir);
        }
    }

    // (Optionally) Clean up after ourselves. :)
    private void cleanUp(Path runPath) {
        // FIXME: Something (possibly PortableGit?) causes this to fail.
        if (programOptions.deleteWorkDirOnFinish) {
            logger.info("Deleting work directory...");

            try {
                IOHelper.deleteRecursively(runPath);
                logger.info("Deleted work directory.");
            } catch (IOException e) {
                logger.error(e);
                logger.warn("Could not delete work directory.");
            }
        }
    }
}
