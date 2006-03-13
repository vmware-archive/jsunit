package net.jsunit;

import junit.framework.TestCase;

public class BrowserLaunchSpecificationTest extends TestCase {

    public void testNoOverride() {
        BrowserLaunchSpecification spec = new BrowserLaunchSpecification("mybrowser.exe");
        assertFalse(spec.hasOverrideUrl());
        assertEquals("mybrowser.exe", spec.getBrowserFileName());
        assertNull(spec.getOverrideUrl());
    }

    public void testOverride() {
        BrowserLaunchSpecification spec = new BrowserLaunchSpecification("mybrowser.exe", "http://www.example.com");
        assertTrue(spec.hasOverrideUrl());
        assertEquals("mybrowser.exe", spec.getBrowserFileName());
        assertEquals("http://www.example.com", spec.getOverrideUrl());
    }

}
