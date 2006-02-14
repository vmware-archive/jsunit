package net.jsunit.plugin.eclipse;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import net.jsunit.plugin.eclipse.resultsui.action.CollapseAllActionTest;
import net.jsunit.plugin.eclipse.resultsui.action.ExpandAllActionTest;
import net.jsunit.plugin.eclipse.resultsui.action.StopActionTest;

public class EclipsePluginTestSuite extends TestCase {

	public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(CollapseAllActionTest.class);
        result.addTestSuite(ExpandAllActionTest.class);
        result.addTestSuite(StopActionTest.class);
        return result;
    }
 
}
