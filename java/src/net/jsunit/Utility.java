package net.jsunit;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class Utility {
    private static boolean logToStandardOut = true;

    public static boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    public static void writeFile(String contents, String fileName) {
        writeFile(contents, new File(fileName));
    }

    public static void writeFile(String contents, File file) {
        try {
            if (file.exists())
                file.delete();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(contents.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List listFromCommaDelimitedString(String string) {
        List result = new ArrayList();
        if (isEmpty(string))
            return result;
        StringTokenizer toker = new StringTokenizer(string, ",");
        while (toker.hasMoreTokens())
            result.add(toker.nextToken());
        return result;
    }

    public static void log(String message, boolean includeDate) {
        if (logToStandardOut) {
            StringBuffer buffer = new StringBuffer();
            if (includeDate) {
                buffer.append(new Date());
                buffer.append(": ");
            }
            buffer.append(message);
            System.out.println(buffer.toString());
        }
    }

    public static void log(String message) {
        log(message, true);
    }

    public static void setLogToStandardOut(boolean b) {
        logToStandardOut = b;
    }
}
