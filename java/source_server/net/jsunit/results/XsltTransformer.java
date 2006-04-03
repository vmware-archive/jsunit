// Copyright 2005 Google Inc.
// All Rights Reserved.
package net.jsunit.results;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

public class XsltTransformer {
    private InputStream xslInputStream;
    private String xml;

    public XsltTransformer(InputStream xslInputStream, String xml) {
        this.xslInputStream = xslInputStream;
        this.xml = xml;
    }

    public void writeToOuputStream(OutputStream out) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslSource = new StreamSource(xslInputStream);
        Templates templates = factory.newTemplates(xslSource);
        Transformer transformer = templates.newTransformer();
        Source xmlSource = new StreamSource(new StringReader(xml));
        transformer.transform(xmlSource, new StreamResult(out));
    }

}
