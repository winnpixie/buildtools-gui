package io.github.winnpixie.logging;

public class LogItem {
    private final LogLevel level;
    private final String message;
    private final Object[] parameters;
    private final Throwable throwable;

    public LogItem(LogLevel level, String message, Object[] parameters, Throwable throwable) {
        this.level = level;
        this.message = message;
        this.parameters = parameters;
        this.throwable = throwable;
    }

    public LogLevel level() {
        return level;
    }

    public String message() {
        return message;
    }

    public Object[] parameters() {
        return parameters;
    }

    public Throwable throwable() {
        return throwable;
    }

    public String formattedMessage(boolean includeLevelPrefix) {
        StringBuilder messageBuilder = new StringBuilder();

        // Prefix
        if (includeLevelPrefix) messageBuilder.append(level.prefix()).append(": ");

        // Message + Parameters
        if (parameters != null && parameters.length > 0) {
            messageBuilder.append(String.format(message, parameters));
        } else {
            messageBuilder.append(message);
        }

        // Thrown
        if (throwable != null) messageBuilder.append('\n')
                .append(throwable.getClass().getSimpleName())
                .append(": ")
                .append(throwable.getMessage());

        return messageBuilder.toString();
    }
}
