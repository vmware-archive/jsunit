package net.jsunit.test;
import net.jsunit.*;
import junit.framework.*;
public abstract class JsUnitTest extends TestCase {
	protected JsUnitResultAcceptor acceptor;
	public void setUp() throws Exception {
		super.setUp();
		acceptor = JsUnitResultAcceptor.instance();
	}
	public void tearDown() throws Exception {
		acceptor.clearResults();
		super.tearDown();
	}
	public JsUnitTest(String name) {
		super(name);
	}
}
