package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.utility.XmlUtility;

public class PlatformTypeTest extends TestCase {

    public void testResolve() throws Exception {
        assertEquals(PlatformType.WINDOWS, PlatformType.resolve("Windows 2000"));
        assertEquals(PlatformType.MACINTOSH, PlatformType.resolve("Mac OS X"));
        assertEquals(PlatformType.LINUX, PlatformType.resolve("Suse Linux"));
    }

    public void testAsXml() throws Exception {
        assertEquals(
                "<platform>" +
                        "<name>Mac</name>" +
                        "<logoPath>/jsunit/images/logo_mac.gif</logoPath>" +
                        "</platform>",
                XmlUtility.asString(PlatformType.MACINTOSH.asXml())
        );
    }
}
