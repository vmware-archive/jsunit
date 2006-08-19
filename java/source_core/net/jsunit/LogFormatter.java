package net.jsunit;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.Date;
import java.text.SimpleDateFormat;

public class LogFormatter extends Formatter {
    public String format(LogRecord record) {
        Date date = new Date(record.getMillis());
        String dateString = SimpleDateFormat.getDateTimeInstance().format(date);
        StringBuffer result = new StringBuffer();
        result.append(dateString);
        result.append(": ");
        result.append(record.getMessage());
        result.append("\r\n");
        return result.toString();
    }
}
