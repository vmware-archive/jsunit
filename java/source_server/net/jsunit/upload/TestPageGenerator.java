package net.jsunit.upload;

import net.jsunit.results.XsltTransformer;

import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestPageGenerator {

    public String generateHTML(String testFragment) throws FileNotFoundException, TransformerException {
        if (testFragment.indexOf("function test") == -1)
            testFragment = "function testSomething() {\n" + testFragment + "\n}";
        String xml = "<testFragmentXml>" + testFragment + "</testFragmentXml>";
        File uploadDirectory = new File("tools");
        File testPageDotXsl = new File(uploadDirectory, "testPage.xsl");
        XsltTransformer transformer = new XsltTransformer(new FileInputStream(testPageDotXsl), xml);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        transformer.writeToOuputStream(outStream);
        return new String(outStream.toByteArray());
    }
}
