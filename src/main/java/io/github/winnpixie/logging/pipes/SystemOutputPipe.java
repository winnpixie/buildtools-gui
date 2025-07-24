package io.github.winnpixie.logging.pipes;

import io.github.winnpixie.logging.LogItem;
import io.github.winnpixie.logging.LogLevel;
import io.github.winnpixie.logging.OutputPipe;

public class SystemOutputPipe implements OutputPipe {
    @Override
    public void print(LogItem item) {
        if (item.level() == LogLevel.SEVERE) {
            System.err.printf("%s%n", item.formattedMessage(true));
        } else {
            System.out.printf("%s%n", item.formattedMessage(true));
        }
    }
}
