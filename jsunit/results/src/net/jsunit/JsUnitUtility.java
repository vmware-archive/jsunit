package net.jsunit;
import java.io.*;
public class JsUnitUtility {
	public static boolean isEmpty(String s) {
		return s == null || s.trim().equals("");
	}
	public static void writeToFile(String contents, String fileName) {
		try {
			File file = new File(fileName);
			if (file.exists())
				file.delete();
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			out.write(contents.getBytes());
			out.close();
		} catch (Exception e) {
			System.out.println("Failed to write log file: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
