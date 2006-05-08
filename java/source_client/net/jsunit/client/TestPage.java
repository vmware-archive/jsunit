package net.jsunit.client;

import net.jsunit.utility.StringUtility;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLScriptElement;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestPage {
    private File testPageFile;

    public TestPage(File testPageFile) {
        this.testPageFile = testPageFile;
        if (!testPageFile.exists())
            throw new RuntimeException("Test Page does not exist: " + testPageFile.getAbsolutePath());
    }

    public File getTestPageFile() {
        return testPageFile;
    }

    public List<File> getReferencedJsFiles() {
        List<File> result = new ArrayList<File>();
        org.cyberneko.html.parsers.DOMParser parser = new org.cyberneko.html.parsers.DOMParser();
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(testPageFile);
            parser.parse(new InputSource(inStream));
            Document document = parser.getDocument();
            NodeList scriptElements = document.getElementsByTagName("script");
            for (int i = 0; i < scriptElements.getLength(); i++) {
                HTMLScriptElement scriptElement = (HTMLScriptElement) scriptElements.item(i);
                String filePath = scriptElement.getSrc();
                if (isValidReferencedJsPath(filePath))
                    result.add(new File(testPageFile.getParentFile(), filePath));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inStream != null)
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private boolean isValidReferencedJsPath(String filePath) {
        return !StringUtility.isEmpty(filePath) && !filePath.toLowerCase().endsWith("jsunitcore.js");
    }

}
