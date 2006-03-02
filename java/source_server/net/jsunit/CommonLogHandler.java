package net.jsunit;

import net.jsunit.logging.JsUnitLogger;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

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
}
