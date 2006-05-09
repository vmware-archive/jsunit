package net.jsunit.utility;

import junit.framework.TestCase;

public class JsUnitURLTest extends TestCase {

    public void testSimple() throws Exception {
        JsUnitURL url = new JsUnitURL("http://www.example.com");
        assertEquals("http://www.example.com", url.asString());
        url.addParameter("foo", "bar");
        assertEquals("http://www.example.com?foo=bar", url.asString());
        url.addParameter("bar", "foo");
        assertEquals("http://www.example.com?foo=bar&bar=foo", url.asString());
    }

    public void testWithExistingParameter() throws Exception {
        JsUnitURL url = new JsUnitURL("http://www.example.com?foo=bar");
        assertEquals("http://www.example.com?foo=bar", url.asString());
        url.addParameter("bar", "foo");
        assertEquals("http://www.example.com?foo=bar&bar=foo", url.asString());
    }
}
