package net.jsunit;

import net.jsunit.logging.JsUnitLogger;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.Level;

class CommonLogHandler extends Handler {
    private JsUnitLogger logger;

    public CommonLogHandler(JsUnitLogger logger) {
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
