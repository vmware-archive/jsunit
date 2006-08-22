package net.jsunit.model;

import net.jsunit.utility.FileUtility;
import net.jsunit.utility.StringUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPage {

    private String fileName;
    private String contents;
    private ReferencedJsFile[] referencedJsFiles;
    private String directory;
    private HTMLSourceInspector htmlSourceInspector;
    private ReferencedJsFileResolver resolver;

    public TestPage() {
    }

    public TestPage(File testPageFile, ReferencedJsFileResolver resolver) {
        if (!testPageFile.exists()) {
            throw new RuntimeException("Test Page does not exist: " + testPageFile.getAbsolutePath());
        }
        this.resolver = resolver;
        this.fileName = testPageFile.getName();
        this.contents = FileUtility.read(testPageFile);
        htmlSourceInspector = new HTMLSourceInspector(contents, resolver);
        directory = testPageFile.getParent();
        resolveDependencies();
    }

    private void resolveDependencies() {
        if (!isSuite())
            resolveReferencedJsFiles();
    }

    TestPage[] resolveReferencedTestPages(File jsUnitDirectory) {
        List<TestPage> testPages = new ArrayList<TestPage>();
        String string = scriptContainingSuite();
        Pattern pattern = Pattern.compile("addTestPage\\((.*)\\)");
        BufferedReader reader = new BufferedReader(new StringReader(string));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String match = matcher.group(1);
                    String noQuotes = StringUtility.stripQuotes(match.trim());
                    File file = new File(jsUnitDirectory.getAbsolutePath(), noQuotes);
                    if (!file.exists())
                        file = new File(directory, noQuotes);
                    testPages.add(new TestPage(file, resolver));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return testPages.toArray(new TestPage[testPages.size()]);
    }

    public boolean isSuite() {
        return htmlSourceInspector.isSuite();
    }

    private String scriptContainingSuite() {
        return htmlSourceInspector.scriptContainingSuite();
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

    private void resolveReferencedJsFiles() {
        List<String> referencedJsFilePaths = findReferencedJsFilePaths();
        List<ReferencedJsFile> result = new ArrayList<ReferencedJsFile>();
        referencedJsFiles = new ReferencedJsFile[referencedJsFilePaths.size()];
        for (String path : referencedJsFilePaths) {
            File referencedJsFile = new File(directory, path);
            result.add(new ReferencedJsFile(referencedJsFile));
        }
        referencedJsFiles = result.toArray(new ReferencedJsFile[referencedJsFilePaths.size()]);
    }

    private List<String> findReferencedJsFilePaths() {
        return htmlSourceInspector.findReferenceJsFilePaths();
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

    public TestPage[] asTestPages(File jsUnitDirectory) {
        if (isSuite())
            return resolveReferencedTestPages(jsUnitDirectory);
        else
            return new TestPage[]{this};
    }
}
