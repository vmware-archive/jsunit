package net.jsunit;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class Utility {

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
        if (isEmpty(string))
            return new ArrayList<String>();
        String[] array = string.split(",");
        for (int i = 0; i < array.length; i++)
            array[i] = array[i].trim();
        return Arrays.asList(array);
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

    public static String asPrettyString(Element element) {
        return new XMLOutputter(Format.getPrettyFormat()).outputString(element);
    }

    public static String asString(Document document) {
        return new XMLOutputter().outputString(document);
    }

    public static Document asXmlDocument(String xmlDocumentString) {
        try {
            return new SAXBuilder().build(new StringReader(xmlDocumentString));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os != null && os.startsWith("Windows");
    }

    public static String stackTraceAsString(Throwable throwable) {
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    public static String commaSeparatedString(List<? extends Object> strings) {
        StringBuffer result = new StringBuffer();
        for (Iterator it = strings.iterator(); it.hasNext();) {
            result.append(it.next());
            if (it.hasNext())
                result.append(",");
        }
        return result.toString();
    }

	public static String osString() {
		StringBuffer result = new StringBuffer();
		result.append(System.getProperty("os.arch"));
		result.append(" - ");
		result.append(System.getProperty("os.name"));
		return result.toString();
	}
}
