package net.jsunit.model;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

public class HeterogenousBrowserGroupTest extends TestCase {

    public void testSimple() throws Exception {
        Browser ie1 = new Browser("c:\\dir\\iexplore.exe", 0);
        Browser ie2 = new Browser("c:\\another\\dir\\iexplore.exe", 1);
        Browser mozilla = new Browser("/usr/bin/mozilla", 2);
        Browser firefox = new Browser("/usr/local/firefox", 3);
        Browser opera = new Browser("/usr/lib/opera", 4);
        List<Browser> browsers = Arrays.asList(new Browser[]{ie1, ie2, mozilla, firefox, opera});

        List<HeterogenousBrowserGroup> groups = HeterogenousBrowserGroup.createFrom(browsers);
        assertEquals(2, groups.size());
    }

}
