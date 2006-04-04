package net.jsunit;

import junit.framework.TestCase;

public class DefaultSkinSourceTest extends TestCase {

    public void testSimple() throws Exception {
        assertEquals(2, new DefaultSkinSource().getSkins().size());
    }

}
