package de.sldk.mc.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

public class ErrorStream extends OutputStream {

    private final Logger logger;
    private final StringBuilder buffer = new StringBuilder();

    public ErrorStream(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void write(int b) throws IOException {
        if (b == '\n') {
            logger.severe(buffer.toString());
            buffer.setLength(0);
        } else {
            buffer.append((char) b);
        }
    }
}
