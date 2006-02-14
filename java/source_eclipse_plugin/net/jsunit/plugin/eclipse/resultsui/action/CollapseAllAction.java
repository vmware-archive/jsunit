/**
 * 
 */
package net.jsunit.plugin.eclipse.resultsui.action;

import net.jsunit.plugin.eclipse.ImageSource;
import net.jsunit.plugin.eclipse.resultsui.ActiveTabSource;

import org.eclipse.jface.action.Action;

public class CollapseAllAction extends Action {
	
	private final ActiveTabSource source;

	public CollapseAllAction(ActiveTabSource source, ImageSource imageSource) {
		this.source = source;
		setText("Collapse all");
		setToolTipText("Collapse all browsers and test pages");
		setDisabledImageDescriptor(imageSource.createImageDescriptor("collapsealldisabled.png"));
		setHoverImageDescriptor(imageSource.createImageDescriptor("collapseallenabled.png"));
		setImageDescriptor(imageSource.createImageDescriptor("collapseallenabled.png"));
	}

	public void run() {
		source.getActiveTab().collapseAll();
	}
}