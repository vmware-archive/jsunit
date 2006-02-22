package net.jsunit.utility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FileUtility {

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

	public static void deleteFile(String fileName) {
	    File file = new File(".", fileName);
	    file.delete();
	}

	public static void deleteDirectory(String directoryName) {
	    File file = new File(directoryName);
	    file.delete();
	}

}
