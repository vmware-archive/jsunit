package net.jsunit.logging;

public interface JsUnitLogger {
	
	public void log(String message, boolean includeDate);

    void flush();
}
