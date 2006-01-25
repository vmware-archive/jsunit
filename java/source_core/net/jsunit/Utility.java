package net.jsunit;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class Utility {
    private static boolean logToStandardOut = true;

    public static boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    public static void writeFile(String contents, String fileName) {
        writeFile(contents, new File(".", fileName));
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

    public static List<String> listFromCommaDelimitedString(String string) {
        List<String> result = new ArrayList<String>();
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

    public static List listWith(Object object1, Object object2) {
        return Arrays.asList(new Object[]{object1, object2});
    }

    public static void deleteFile(String fileName) {
        File file = new File(".", fileName);
        file.delete();
    }

    public static void deleteDirectory(String directoryName) {
        File file = new File(directoryName);
        file.delete();
    }

    public static String asString(Element element) {
        return new XMLOutputter().outputString(element);
    }

    public static String asString(Document document) {
        return new XMLOutputter().outputString(document);
    }

	public static Document asXmlDocument(String xmlDocumentString) {
		try {
			return new DOMBuilder().build(new ByteArrayInputStream(xmlDocumentString.getBytes()));
		} catch (JDOMException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
