package net.jsunit.plugin.eclipse.resultsui;

import net.jsunit.plugin.eclipse.JsUnitPlugin;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
	
	public String getColumnText(Object obj, int index) {
		return getText(obj);
	}
	
	public String getText(Object obj) {
		Node node = (Node) obj;
		return node.getDisplayString();		
	}
	
	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}
	
	public Image getImage(Object obj) {
		Node node = (Node) obj;
		return JsUnitPlugin.createImage(node.getImageName());
	}
	
}