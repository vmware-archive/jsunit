package net.jsunit.plugin.eclipse;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import net.jsunit.plugin.eclipse.resultsui.NodeLabelProviderTest;
import net.jsunit.plugin.eclipse.resultsui.action.CollapseAllActionTest;
import net.jsunit.plugin.eclipse.resultsui.action.ExpandAllActionTest;
import net.jsunit.plugin.eclipse.resultsui.action.StopActionTest;
import net.jsunit.plugin.eclipse.resultsui.action.ToggleFullyQualifiedNodeNamesActionTest;
import net.jsunit.plugin.eclipse.resultsui.node.BrowserResultNodeTest;
import net.jsunit.plugin.eclipse.resultsui.node.TestCaseResultNodeTest;
import net.jsunit.plugin.eclipse.resultsui.node.TestPageResultNodeTest;

public class EclipsePluginTestSuite extends TestCase {

	public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(BrowserResultNodeTest.class);
        result.addTestSuite(CollapseAllActionTest.class);
        result.addTestSuite(ExpandAllActionTest.class);
        result.addTestSuite(NodeLabelProviderTest.class);
        result.addTestSuite(StopActionTest.class);
        result.addTestSuite(TestCaseResultNodeTest.class);
        result.addTestSuite(TestPageResultNodeTest.class);
        result.addTestSuite(ToggleFullyQualifiedNodeNamesActionTest.class);        
        return result;
    }
 
}
