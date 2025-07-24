package io.github.winnpixie.btgui.configurations;

public class ProgramConfiguration {
    private final boolean downloadBuildTools;
    private final boolean isolateProcesses;
    private final boolean showOutputOnFinish;
    private final boolean deleteWorkOnFinish;

    private ProgramConfiguration(Builder builder) {
        this.downloadBuildTools = builder.downloadBuildTools;
        this.isolateProcesses = builder.isolateProcesses;
        this.showOutputOnFinish = builder.showOutputOnFinish;
        this.deleteWorkOnFinish = builder.deleteWorkOnFinish;
    }

    public boolean downloadBuildTools() {
        return downloadBuildTools;
    }

    public boolean isolateProcesses() {
        return isolateProcesses;
    }

    public boolean showOutputOnFinish() {
        return showOutputOnFinish;
    }

    public boolean deleteWorkOnFinish() {
        return deleteWorkOnFinish;
    }

    public static class Builder {
        private boolean downloadBuildTools = true;
        private boolean isolateProcesses = true;
        private boolean showOutputOnFinish = true;
        private boolean deleteWorkOnFinish = false;

        public Builder downloadBuildTools(boolean download) {
            this.downloadBuildTools = download;
            return this;
        }

        public Builder isolateProcesses(boolean isolate) {
            this.isolateProcesses = isolate;
            return this;
        }

        public Builder showOutputOnFinish(boolean show) {
            this.showOutputOnFinish = show;
            return this;
        }

        public Builder deleteWorkOnFinish(boolean delete) {
            this.deleteWorkOnFinish = delete;
            return this;
        }

        public ProgramConfiguration build() {
            return new ProgramConfiguration(this);
        }
    }
}
