package io.github.winnpixie.btgui.utilities;

import io.github.winnpixie.btgui.tasks.BuildToolsTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessHelper {
    private static final AtomicInteger PROCESS_COUNT = new AtomicInteger(0);

    public static int getProcessCount() {
        return PROCESS_COUNT.get();
    }

    public static int addProcess() {
        return PROCESS_COUNT.incrementAndGet();
    }

    public static int removeProcess() {
        return PROCESS_COUNT.decrementAndGet();
    }

    public static BuildToolsTask createProcess(List<String> javaCommand, List<String> programArgs) {
        return new BuildToolsTask(javaCommand, programArgs);
    }
}
