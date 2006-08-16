package net.jsunit.model;

import net.jsunit.utility.StringUtility;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLScriptElement;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLSourceInspector {
    private String html;
    private NodeList cachedScriptElements;

    public HTMLSourceInspector(String html) {
        this.html = html;
        cachedScriptElements = scriptElements();
    }

    public List<String> scripts() {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < cachedScriptElements.getLength(); i++) {
            HTMLScriptElement scriptElement = (HTMLScriptElement) cachedScriptElements.item(i);
            if (StringUtility.isEmpty(scriptElement.getSrc()))
                result.add(scriptElement.getText());
        }
        return result;
    }

    public List<String> findReferenceJsFilePaths() {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < cachedScriptElements.getLength(); i++) {
            HTMLScriptElement scriptElement = (HTMLScriptElement) cachedScriptElements.item(i);
            String filePath = scriptElement.getSrc();
            if (isNonEmptyNonJsUnitReferencedJsFile(filePath))
                result.add(filePath);
        }
        return result;
    }

    private boolean isNonEmptyNonJsUnitReferencedJsFile(String filePath) {
        return !StringUtility.isEmpty(filePath) && !filePath.toLowerCase().endsWith("jsunitcore.js");
    }

    private NodeList scriptElements() {
        org.cyberneko.html.parsers.DOMParser parser = new org.cyberneko.html.parsers.DOMParser();
        try {
            parser.parse(new InputSource(new StringReader(html)));
            Document document = parser.getDocument();
            return document.getElementsByTagName("script");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSuite() {
        return scriptContainingSuite() != null;
    }

    public String scriptContainingSuite() {
        Pattern pattern = Pattern.compile("function\\s+suite\\s*\\(\\s*\\)");
        for (String script : scripts()) {
            Matcher matcher = pattern.matcher(script);
            if (matcher.find())
                return script;
        }
        return null;
    }
}
