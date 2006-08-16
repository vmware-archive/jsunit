package net.jsunit.utility;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Node;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtility {

    public static String asString(Element element) {
        return new XMLOutputter().outputString(element);
    }

    public static String asPrettyString(Element element) {
        return new XMLOutputter(Format.getPrettyFormat()).outputString(element);
    }

    public static String asPrettyString(Document document) {
        return new XMLOutputter(Format.getPrettyFormat()).outputString(document.getRootElement());
    }

    public static String asString(Document document) {
        return new XMLOutputter().outputString(document);
    }

    public static Document asXmlDocument(String xmlDocumentString) {
        try {
            return new SAXBuilder().build(new StringReader(xmlDocumentString));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String asString(Node node) {
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
