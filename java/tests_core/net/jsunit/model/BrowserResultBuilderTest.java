package net.jsunit.model;

import net.jsunit.utility.FileUtility;

import java.io.File;

public class BrowserResultBuilderTest extends BrowserResultTestCase {

    public void testBuildFromXmlFile() {
        File file = null;
        try {
            FileUtility.write(new File("resultXml.xml"), expectedXmlFragment);
            file = new File("resultXml.xml");
            BrowserResult reconstitutedResult = new BrowserResultBuilder().build(file);
            assertEquals(BrowserResult.class, reconstitutedResult.getClass());
            assertFields(reconstitutedResult);
        } finally {
            if (file != null)
                file.delete();
        }
    }

    public void testBuildFromXmlDocument() {
        BrowserResult reconstitutedResult = new BrowserResultBuilder().build(result.asXmlDocument());
        assertFields(reconstitutedResult);
    }

}
