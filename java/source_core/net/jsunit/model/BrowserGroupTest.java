package net.jsunit.model;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

public class BrowserGroupTest extends TestCase {

    public void testSimple() throws Exception {
        Browser sameType1A = new Browser("c:\\dir\\iexplore.exe", 0);
        Browser sameType1B = new Browser("c:\\another\\dir\\iexplore.exe", 1);
        Browser sameType2A = new Browser("/usr/bin/mozilla", 2);
        Browser sameType2B = new Browser("/usr/local/firefox", 3);
        Browser differentType = new Browser("/usr/lib/opera", 4);
        List<Browser> browsers = Arrays.asList(new Browser[]{sameType1A, sameType1B, sameType2A, sameType2B, differentType});

        List<BrowserGroup> groups = BrowserGroup.createFrom(browsers);
//        assertEquals(3, groups.size());
    }

}
