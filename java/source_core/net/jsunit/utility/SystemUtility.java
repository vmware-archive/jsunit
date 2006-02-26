package net.jsunit.utility;

public class SystemUtility {

    public static String osArchitecture() {
        return System.getProperty("os.arch");
    }

    public static String osName() {
        return System.getProperty("os.name");
    }

    public static String osString() {
        StringBuffer result = new StringBuffer();
        result.append(osArchitecture());
        result.append(" - ");
        result.append(osName());
        return result.toString();
    }

    public static String jsUnitVersion() {
        //TODO: get this from jsUnitCore.js
        return "2.2";
    }
}
