package net.jsunit.utility;

import java.io.*;

public class FileUtility {

    public static void delete(File file) {
        if (file.exists())
            if (!file.delete())
                throw new RuntimeException("Couldn't delete file: " + file.getAbsolutePath());
    }

    public static void write(File file, String contents) {
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

    public static String read(File file) {
        StringBuffer buffer = new StringBuffer();
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            int aByte;
            while ((aByte = in.read()) != -1)
                buffer.append((char) aByte);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    public static void deleteDirectoryAndContents(String directoryName) {
        deleteDirectoryAndContents(new File(directoryName));
    }

    public static boolean doesFileExist(String path) {
        return new File(path).exists();
    }

    public static File jsUnitPath() {
        String directory = System.getProperty("jsUnitPath");
        if (directory == null)
            directory = ".";
        return new File(directory);
    }

    public static void deleteDirectoryAndContents(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory())
                deleteDirectoryAndContents(file.getAbsolutePath());
            else
                FileUtility.delete(file);
        }
        FileUtility.delete(directory);
    }
}
