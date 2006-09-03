package net.jsunit.model;

import net.jsunit.utility.XmlUtility;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLSourceInspector {
    private String html;
    private List<Node> cachedScriptElements;
    private ReferencedJsFileResolver referencedJsFileResolver;

    public HTMLSourceInspector(String html, ReferencedJsFileResolver resolver) {
        this.html = html;
        this.referencedJsFileResolver = resolver;
        cachedScriptElements = scriptElements();
    }

    public List<String> scripts() {
        List<String> result = new ArrayList<String>();
        for (Node scriptElement : cachedScriptElements) {
            Node src = XmlUtility.srcAttribute(scriptElement);
            if (src == null)
                result.add(scriptElement.getTextContent());            
        }
        return result;
    }

    public List<String> findReferenceJsFilePaths() {
        return referencedJsFileResolver.resolve(cachedScriptElements);
    }

    private List<Node> scriptElements() {
        org.cyberneko.html.parsers.DOMParser parser = new org.cyberneko.html.parsers.DOMParser();
        try {
            parser.parse(new InputSource(new StringReader(html)));
            Document document = parser.getDocument();
            NodeList nodeList = document.getElementsByTagName("SCRIPT");
            List<Node> result = new ArrayList<Node>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                result.add(node);
            }
            return result;
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
