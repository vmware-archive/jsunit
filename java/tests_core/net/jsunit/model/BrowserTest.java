package net.jsunit.model;

import junit.framework.TestCase;

public class BrowserTest extends TestCase {

    public void testSimple() throws Exception {
        Browser browser = new Browser("c:\\program files\\internet explorer\\iexplore.exe", 4);
        assertEquals("c:\\program files\\internet explorer\\iexplore.exe", browser.getFileName());
        assertEquals(4, browser.getId());
        assertTrue(browser.hasId(4));
        assertFalse(browser.hasId(2));
        assertNull(browser.getKillCommand());
    }

    public void testKillCommand() throws Exception {
        Browser browser = new Browser("mybrowser.exe", 0);
        assertEquals("mybrowser.exe", browser.getFileName());
        assertNull(browser.getKillCommand());

        browser = new Browser("mybrowser.exe;", 0);
        assertEquals("mybrowser.exe", browser.getFileName());
        assertNull(browser.getKillCommand());

        browser = new Browser("mybrowser.exe;kill-mybrowser.bat", 0);
        assertEquals("mybrowser.exe", browser.getFileName());
        assertEquals("kill-mybrowser.bat", browser.getKillCommand());
    }

    public void testDisplayName() throws Exception {
        Browser browser = new Browser("c:\\dir\\browser.exe;;Internet Explorer 5.5", 3);
        assertEquals("c:\\dir\\browser.exe", browser.getFileName());
        assertEquals("Internet Explorer 5.5", browser.getDisplayString());
    }

    public void testKillCommandAndDisplayName() throws Exception {
        Browser browser = new Browser("/usr/local/mozilla;/usr/local/kill-moz.sh;Mozilla 7.4", 2);
        assertEquals("/usr/local/mozilla", browser.getFileName());
        assertEquals("/usr/local/kill-moz.sh", browser.getKillCommand());
        assertEquals("Mozilla 7.4", browser.getDisplayString());
    }

    public void testDisplayNameWhenNoneGiven() throws Exception {
        Browser browser = new Browser("mybrowser.exe", 4);
        assertEquals("mybrowser.exe", browser.getDisplayString());
    }

    public void testBrowserType() throws Exception {
        Browser browser = new Browser("/some/path/to/opera.exe", 2);
        assertEquals(BrowserType.OPERA, browser.getType());
    }

    public void testUnknownBrowserType() throws Exception {
        Browser browser = new Browser("/some/path/to/something/unknown.sh", 2);
        assertNull(browser.getType());
    }

}
