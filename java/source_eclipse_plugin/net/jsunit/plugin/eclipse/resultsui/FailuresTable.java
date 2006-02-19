package net.jsunit.plugin.eclipse.resultsui;

import net.jsunit.plugin.eclipse.JsUnitPlugin;
import net.jsunit.plugin.eclipse.resultsui.node.TestCaseResultNode;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class FailuresTable extends Table {

	private ContentProvider contentProvider;
	private NodeLabelProvider labelProvider;

	public FailuresTable(Composite parent, int style, ContentProvider provider, NodeLabelProvider labelProvider) {
		super(parent, style);
		this.contentProvider = provider;
		this.labelProvider = labelProvider;
	}

	public void refresh() {
		JsUnitPlugin.getDisplay().syncExec(
			new Runnable() {
				public void run() {
					removeAll();
					repopulateFromContentProvider();
				}
			}
		);
	}
	
	private void repopulateFromContentProvider() {
		for (TestCaseResultNode node : contentProvider.getProblemTestCaseResultNodes()) {
			TableItem tableItem = new TestCaseResultTableItem(this, node, labelProvider);
			showItem(tableItem);
		}
	}
	
	protected void checkSubclass () {
	}
}