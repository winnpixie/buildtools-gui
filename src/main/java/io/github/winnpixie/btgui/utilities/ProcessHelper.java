package io.github.winnpixie.btgui.utilities;

import io.github.winnpixie.btgui.options.BuildToolsOptions;
import io.github.winnpixie.btgui.options.ProgramOptions;
import io.github.winnpixie.btgui.tasks.BuildToolsTask;

import java.util.concurrent.atomic.AtomicInteger;

public class ProcessHelper {
    public static final AtomicInteger PROCESS_COUNTER = new AtomicInteger(0);
    public static int PID_TICKER = 0;

    public static BuildToolsTask createProcess(ProgramOptions programOptions, BuildToolsOptions buildToolsOptions) {
        return new BuildToolsTask(programOptions, buildToolsOptions);
    }
}
