package net.jsunit.logging;

import java.util.Date;

public class SystemOutJsUnitLogger implements JsUnitLogger {

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
