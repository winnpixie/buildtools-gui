package io.github.winnpixie.btgui.tasks;

import io.github.winnpixie.btgui.BuildToolsGUI;
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
    private final PipedLogger logger;

    private final ProgramConfiguration programConfiguration;
    private final JavaConfiguration javaConfiguration;
    private final BuildToolsConfiguration buildToolsConfiguration;

    private Process process;

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
        if (!programConfiguration.isolateProcesses()
                && BuildToolsGUI.JOB_TRACKER.get() > 1) {
            logger.log(LogLevel.SEVERE, "Multiple workers are prohibited when isolation is disabled!");
            return -47;
        }

        List<String> javaCommand = javaConfiguration.toList();
        List<String> buildToolsArguments = buildToolsConfiguration.toList();
        logger.info("JAVA COMMAND: %s", String.join(" ", javaCommand));
        logger.info("PROGRAM ARGUMENTS: %s", String.join(" ", buildToolsArguments));

        logger.info("Initializing work directory");
        Path workingPath = createWorkPath();
        if (workingPath == null) {
            return -1337;
        }

        Path buildToolsPath = Paths.get(SystemHelper.CURRENT_DIRECTORY, "BuildTools.jar");
        if (programConfiguration.downloadBuildTools()) {
            logger.info("Downloading 'BuildTools.jar' from 'https://hub.spigotmc.org/'");

            byte[] binary = download();
            if (binary.length == 0
                    || !save(buildToolsPath, binary)) {
                return -420;
            }
        }

        logger.info("Copying BuildTools.jar to work directory");
        if (!copyToWorkingPath(buildToolsPath, workingPath)) {
            return -69;
        }

        ProcessBuilder processBuilder = buildProcess(workingPath);
        logger.info("Full Command:");
        logger.info(String.join(" ", processBuilder.command()));

        logger.info("Running BuildTools...");
        return run(processBuilder, workingPath);
    }

    private byte[] download() {
        byte[] binary;

        try {
            binary = IOHelper.getBinary("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar");
        } catch (IOException ioe) {
            logger.log(LogLevel.SEVERE, ioe, "BuildTools.jar could not be downloaded");

            binary = new byte[0];
        }

        return binary;
    }

    private boolean save(Path path, byte[] data) {
        try {
            Files.write(path, data);
        } catch (IOException ioe) {
            logger.log(LogLevel.SEVERE, ioe, "BuildTools.jar could not be written to disk");

            return false;
        }

        return true;
    }

    private Path createWorkPath() {
        Path workPath = Paths.get(SystemHelper.CURRENT_DIRECTORY, !programConfiguration.isolateProcesses() ?
                "BuildTools" : String.format("BuildTools-%d", System.currentTimeMillis()));

        if (Files.notExists(workPath)) {
            try {
                Files.createDirectory(workPath);
            } catch (IOException ioe) {
                logger.log(LogLevel.SEVERE, ioe, "The work directory could not be initialized");

                return null;
            }
        }

        logger.info("Work directory: %s", workPath);
        return workPath;
    }

    private boolean copyToWorkingPath(Path src, Path dst) {
        try {
            Files.copy(src, dst.resolve("BuildTools.jar"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            logger.log(LogLevel.SEVERE, ioe, "BuildTools could not be copied to the work directory");

            return false;
        }

        return true;
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

    private int run(ProcessBuilder processBuilder, Path workingPath) {
        int exitCode;
        long startTime = System.currentTimeMillis();

        try {
            this.process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                reader.lines().forEach(line -> logger.info("[BT] %s", line));
            }

            while (process.isAlive()) {
                process.destroy();
            }

            exitCode = process.exitValue();
        } catch (Exception e) {
            logger.log(LogLevel.SEVERE, e, "Error running BuildTools");
            exitCode = -42;
        }

        logger.info("BuildsTools took %.3fs", (System.currentTimeMillis() - startTime) / 1000.0);

        if (exitCode == 0 && programConfiguration.showOutputOnFinish()) {
            File outputDir = buildToolsConfiguration.outputDirectory().isEmpty() ?
                    workingPath.toFile() : new File(buildToolsConfiguration.outputDirectory());

            logger.info("Opening '%s' in system file explorer", outputDir.getPath());
            SystemHelper.openFolder(outputDir);
        }

        cleanUp(workingPath);

        return exitCode;
    }

    // (Optionally) Clean up after ourselves. :)
    private void cleanUp(Path path) {
        if (!programConfiguration.deleteWorkOnFinish()) {
            return;
        }

        logger.info("Deleting work directory...");

        try {
            // FIXME: Something (PortableGit?) causes this to fail.
            IOHelper.deleteRecursively(path);

            logger.info("Deleted work directory");
        } catch (IOException ioe) {
            logger.log(LogLevel.WARNING, ioe, "Issue deleting work directory");
        }
    }

    public void terminate() {
        logger.info("The termination request has been received");

        if (process == null) {
            logger.warning("The process has not yet been created!");
            return;
        }

        if (!process.isAlive()) {
            logger.warning("The process is not alive!");
            return;
        }

        while (process.isAlive()) {
            // FIXME: Something causes this to be ignored?
            process.destroyForcibly();
        }

        logger.info("The process has been terminated");
    }
}
