package net.jsunit.utility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StringUtility {

	public static boolean isEmpty(String s) {
	    return s == null || s.trim().equals("");
	}

	public static List<String> listFromCommaDelimitedString(String string) {
	    if (isEmpty(string))
	        return new ArrayList<String>();
	    String[] array = string.split(",");
	    for (int i = 0; i < array.length; i++)
	        array[i] = array[i].trim();
	    return Arrays.asList(array);
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

	public static String unqualify(String string) {
		int indexOfForwardSlash = string.lastIndexOf("/");
		if (indexOfForwardSlash >= 0 && indexOfForwardSlash < string.length())
			string = string.substring(indexOfForwardSlash + 1);
		int indexOfBackSlash = string.lastIndexOf("\\");
		if (indexOfBackSlash >= 0 && indexOfBackSlash < string.length())
			string = string.substring(indexOfBackSlash + 1);
		return string;
	}

}
