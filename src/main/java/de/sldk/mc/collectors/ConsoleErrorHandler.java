package de.sldk.mc.collectors;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ConsoleErrorHandler extends Handler {

    private final AtomicInteger errorCounter;

    public ConsoleErrorHandler(AtomicInteger errorCounter) {
        this.errorCounter = errorCounter;
    }

    @Override
    public void publish(LogRecord record) {
        if (isError(record)) {
            errorCounter.incrementAndGet();
        }
    }

    private boolean isError(LogRecord record) {
        return record.getLevel() == Level.SEVERE
                || record.getMessage().toLowerCase().contains("error")
                || record.getMessage().toLowerCase().contains("exception")
                || record.getThrown() != null;
    }

    @Override
    public void flush() {
        // ---
    }

    @Override
    public void close() throws SecurityException {
        // ---
    }
}
