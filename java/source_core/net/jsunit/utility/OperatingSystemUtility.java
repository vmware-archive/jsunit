package net.jsunit.utility;

public class OperatingSystemUtility {

	public static boolean isWindows() {
	    String os = System.getProperty("os.name");
	    return os != null && os.startsWith("Windows");
	}
	
    public static boolean isMacintosh() {
        String os = System.getProperty("os.name");
        return os != null && os.startsWith("Mac");
    }

	public static String osString() {
		StringBuffer result = new StringBuffer();
		result.append(System.getProperty("os.arch"));
		result.append(" - ");
		result.append(System.getProperty("os.name"));
		return result.toString();
	}

}
