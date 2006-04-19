package net.jsunit.uploaded;

import net.jsunit.results.XsltTransformer;
import net.jsunit.model.TestPage;
import net.jsunit.model.ReferencedJsFile;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLScriptElement;
import org.xml.sax.InputSource;

import javax.xml.transform.*;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class TestPageFactory {

    public TestPage fromFragment(String fragment) {
        return new TestPage(generateTestPageHTMLFromFragment(fragment), true);
    }

    public TestPage fromUploaded(String html, ReferencedJsFile... referencedJsFiles) {
        html = modifyJsIncludesToPointToReferencedJsFiles(html, Arrays.asList(referencedJsFiles));
        return new TestPage(html, false, referencedJsFiles);
    }

    private String generateTestPageHTMLFromFragment(String testFragment) {
        if (testFragment.indexOf("function test") == -1)
            testFragment = "function testSomething() {\n" + testFragment + "\n}";
        String xml = "<testFragmentXml>" + testFragment + "</testFragmentXml>";
        File uploadDirectory = new File("tools");
        File testPageXsl = new File(uploadDirectory, "testPage.xsl");
        try {
            XsltTransformer transformer = new XsltTransformer(new FileInputStream(testPageXsl), xml);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            transformer.writeToOuputStream(outStream);
            return new String(outStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String modifyJsIncludesToPointToReferencedJsFiles(String rawHTML, List<ReferencedJsFile> referencedJsFiles) {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(new StringReader(rawHTML)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Document document = parser.getDocument();
        NodeList scriptElements = document.getElementsByTagName("script");
        for (int i = 0; i < scriptElements.getLength(); i++) {
            HTMLScriptElement scriptElement = (HTMLScriptElement) scriptElements.item(i);
            replaceSourceAttributeOn(scriptElement, referencedJsFiles);
        }
        return asString(document);
    }

    private void replaceSourceAttributeOn(HTMLScriptElement scriptElement, List<ReferencedJsFile> referencedJsFiles) {
        String source = scriptElement.getSrc();
        if (source != null) {
            if (source.endsWith("jsUnitCore.js"))
                scriptElement.setSrc("../app/jsUnitCore.js");
            else {
                for (ReferencedJsFile referencedJsFile : referencedJsFiles) {
                    if (source.endsWith(referencedJsFile.getOriginalFileName())) {
                        scriptElement.setSrc(referencedJsFile.getFileName());
                        return;
                    }
                }
            }
        }
    }

    private String asString(Node node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

}
