/**
 * 
 */
package net.jsunit.plugin.eclipse.resultsui.action;

import net.jsunit.plugin.eclipse.ImageSource;
import net.jsunit.plugin.eclipse.resultsui.ActiveTabSource;

import org.eclipse.jface.action.Action;

public class ExpandAllAction extends Action {
	private final ActiveTabSource activeTabSource;

	public ExpandAllAction(ActiveTabSource activeTabSource, ImageSource imageSource) {
		this.activeTabSource = activeTabSource;
		setText("Expand all");
		setToolTipText("Expand all browsers and test pages");
		setDisabledImageDescriptor(imageSource.createImageDescriptor("expandalldisabled.png"));
		setHoverImageDescriptor(imageSource.createImageDescriptor("expandallenabled.png"));
		setImageDescriptor(imageSource.createImageDescriptor("expandallenabled.png"));
	}

	public void run() {
		activeTabSource.getActiveTab().expandAll();
	}
}