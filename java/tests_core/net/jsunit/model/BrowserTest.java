package net.jsunit.model;

import junit.framework.TestCase;
import net.jsunit.utility.XmlUtility;

public class BrowserTest extends TestCase {

    public void testSimple() throws Exception {
        Browser browser = new Browser("c:\\program files\\internet explorer\\iexplore.exe", 4);
        assertEquals("c:\\program files\\internet explorer\\iexplore.exe", browser.getStartCommand());
        assertEquals(4, browser.getId());
        assertTrue(browser.hasId(4));
        assertFalse(browser.hasId(2));
        assertNull(browser.getKillCommand());
    }

    public void testKillCommand() throws Exception {
        Browser browser = new Browser("mybrowser.exe", 0);
        assertEquals("mybrowser.exe", browser.getStartCommand());
        assertNull(browser.getKillCommand());

        browser = new Browser("mybrowser.exe;", 0);
        assertEquals("mybrowser.exe", browser.getStartCommand());
        assertNull(browser.getKillCommand());

        browser = new Browser("mybrowser.exe;kill-mybrowser.bat", 0);
        assertEquals("mybrowser.exe", browser.getStartCommand());
        assertEquals("kill-mybrowser.bat", browser.getKillCommand());
    }

    public void testDisplayName() throws Exception {
        Browser browser = new Browser("c:\\dir\\browser.exe;;Internet Explorer 5.5", 3);
        assertEquals("c:\\dir\\browser.exe;;Internet Explorer 5.5", browser.getFullFileName());
        assertEquals("c:\\dir\\browser.exe", browser.getStartCommand());
        assertEquals("Internet Explorer 5.5", browser.getDisplayName());

        browser = new Browser("c:\\dir\\iexplore.exe;", 9);
        assertEquals(BrowserType.INTERNET_EXPLORER.getDisplayName(), browser.getDisplayName());

        browser = new Browser("c:\\dir\\mybrowser.exe;", 9);
        assertEquals("c:\\dir\\mybrowser.exe", browser.getDisplayName());
    }

    public void testKillCommandAndDisplayName() throws Exception {
        Browser browser = new Browser("/usr/local/mozilla;/usr/local/kill-moz.sh;Mozilla 7.4", 2);
        assertEquals("/usr/local/mozilla", browser.getStartCommand());
        assertEquals("/usr/local/kill-moz.sh", browser.getKillCommand());
        assertEquals("Mozilla 7.4", browser.getDisplayName());
    }

    public void testDisplayNameWhenNoneGiven() throws Exception {
        Browser browser = new Browser("mybrowser.exe", 4);
        assertEquals("mybrowser.exe", browser.getDisplayName());
    }

    public void testBrowserType() throws Exception {
        Browser browser = new Browser("/some/path/to/opera.exe", 2);
        assertEquals(BrowserType.OPERA, browser._getType());
    }

    public void testUnknownBrowserType() throws Exception {
        Browser browser = new Browser("/some/path/to/something/unknown.sh", 2);
        assertEquals(BrowserType.UNKNOWN, browser._getType());
    }

    public void testConflictsWith() throws Exception {
        Browser mozilla = new Browser("/usr/bin/mozilla", 0);
        Browser opera = new Browser("/usr/bin/opera", 1);
        assertFalse(mozilla.conflictsWith(opera));
        assertTrue(mozilla.conflictsWith(mozilla));
    }

    public void testAsXml() throws Exception {
        Browser browser = new Browser("/usr/bin/start_mozilla.sh;/usr/bin/stop_mozilla.sh;Mozilla 0.9.2.1", 5);
        assertEquals(
                "<browser>" +
                        "<fullFileName>/usr/bin/start_mozilla.sh;/usr/bin/stop_mozilla.sh;Mozilla 0.9.2.1</fullFileName>" +
                        "<id>5</id>" +
                        "<displayName>Mozilla 0.9.2.1</displayName>" +
                        "<logoPath>images/logo_mozilla.gif</logoPath>" +
                        "</browser>",
                XmlUtility.asString(browser.asXml())
        );
    }

    public void testCompareTo() throws Exception {
        Browser browser0 = new Browser("browser0.exe", 0);
        Browser browser1 = new Browser("browser1.exe", 1);
        assertTrue(browser0.compareTo(browser1) < 0);
    }

}
