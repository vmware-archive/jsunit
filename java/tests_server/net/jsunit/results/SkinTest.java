package net.jsunit.results;

import junit.framework.TestCase;

import java.io.File;

public class SkinTest extends TestCase {

    public void testSimple() throws Exception {
        Skin skin = new Skin(3, new File(".", "my_cool_jsunit_skin.xsl"));
        assertEquals(3, skin.getId());
        assertEquals("my_cool_jsunit_skin", skin.getDisplayName());
    }
}
