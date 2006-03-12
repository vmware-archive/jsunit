package net.jsunit;

import net.jsunit.logging.StatusLogger;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class CommonLogHandler extends Handler {
    private StatusLogger logger;

    public CommonLogHandler(StatusLogger logger) {
        this.logger = logger;
    }

    public void publish(LogRecord record) {
        logger.log(record.getLevel().toString() + " " + record.getMessage(), true);
    }

    public void flush() {
        logger.flush();
    }

    public void close() throws SecurityException {
    }

    public void addThirdPartyLogger(Logger logger) {
        logger.setLevel(Level.SEVERE);
        logger.addHandler(this);
        logger.setUseParentHandlers(false);
    }
}
