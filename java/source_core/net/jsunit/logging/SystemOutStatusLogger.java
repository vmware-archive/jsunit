package net.jsunit.logging;

import java.util.Date;

public class SystemOutStatusLogger implements StatusLogger {

	public void log(String message) {
		System.out.println(new Date() + ": JsUnit status: " + message);
	}

}
