package net.jsunit.logging;

import java.util.Date;

public class SystemOutStatusLogger implements StatusLogger {

    public void log(String message, boolean includeDate) {
        StringBuffer buffer = new StringBuffer();
        if (includeDate) {
            buffer.append(new Date());
            buffer.append(": ");
        }
        buffer.append(message);
        System.out.println(buffer.toString());
    }

    public void flush() {
        System.out.flush();
    }

}
