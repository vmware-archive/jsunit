package net.jsunit.model;

import net.jsunit.utility.FileUtility;
import net.jsunit.utility.StringUtility;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLScriptElement;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TestPage {

    private String fileName;
    private String contents;
    private ReferencedJsFile[] referencedJsFiles;

    public TestPage() {
    }

    public TestPage(File testPageFile) {
        if (!testPageFile.exists())
            throw new RuntimeException("Test Page does not exist: " + testPageFile.getAbsolutePath());
        this.fileName = testPageFile.getName();
        this.contents = FileUtility.read(testPageFile);
        resolveReferencedJsFiles(testPageFile.getParent());
    }

    public String getContents() {
        return contents;
    }

    public ReferencedJsFile[] getReferencedJsFiles() {
        return referencedJsFiles;
    }

    public String getFileName() {
        return fileName;
    }

    private void resolveReferencedJsFiles(String parent) {
        List<String> referencedJsFilePaths = findReferencedJsFilePaths();
        List<ReferencedJsFile> result = new ArrayList<ReferencedJsFile>();
        referencedJsFiles = new ReferencedJsFile[referencedJsFilePaths.size()];
        for (String path : referencedJsFilePaths) {
            File referencedJsFile = new File(parent, path);
            result.add(new ReferencedJsFile(referencedJsFile));
        }
        referencedJsFiles = result.toArray(new ReferencedJsFile[referencedJsFilePaths.size()]);
    }

    private List<String> findReferencedJsFilePaths() {
        List<String> result = new ArrayList<String>();
        org.cyberneko.html.parsers.DOMParser parser = new org.cyberneko.html.parsers.DOMParser();
        try {
            parser.parse(new InputSource(new StringReader(contents)));
            Document document = parser.getDocument();
            NodeList scriptElements = document.getElementsByTagName("script");
            for (int i = 0; i < scriptElements.getLength(); i++) {
                HTMLScriptElement scriptElement = (HTMLScriptElement) scriptElements.item(i);
                String filePath = scriptElement.getSrc();
                if (isValidReferencedJsPath(filePath))
                    result.add(filePath);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidReferencedJsPath(String filePath) {
        return !StringUtility.isEmpty(filePath) && !filePath.toLowerCase().endsWith("jsunitcore.js");
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setReferencedJsFiles(ReferencedJsFile[] referencedJsFiles) {
        this.referencedJsFiles = referencedJsFiles;
    }

}
