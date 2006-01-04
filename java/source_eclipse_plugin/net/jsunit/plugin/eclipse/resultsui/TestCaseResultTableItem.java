package net.jsunit.plugin.eclipse.resultsui;

import net.jsunit.plugin.eclipse.JsUnitPlugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class TestCaseResultTableItem extends TableItem {

	public TestCaseResultTableItem(Table table, TestCaseResultNode node) {
		super(table, SWT.NONE);
		setText(node.getDisplayStringWithBrowserFileName());
		setImage(JsUnitPlugin.createImage(node.getImageName()));
		setData(node.getResult());
	}
	
	protected void checkSubclass () {
	}


}