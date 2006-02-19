package net.jsunit.plugin.eclipse.resultsui.action;

import net.jsunit.plugin.eclipse.ImageSource;
import net.jsunit.plugin.eclipse.resultsui.ActiveTabSource;
import net.jsunit.plugin.eclipse.resultsui.NodeLabelProvider;

import org.eclipse.jface.action.Action;

public class ToggleFullyQualifiedNodeNamesAction extends Action {

	private final NodeLabelProvider labelProvider;
	private final ActiveTabSource activeTabSource;

	public ToggleFullyQualifiedNodeNamesAction(ActiveTabSource activeTabSource, ImageSource imageSource, NodeLabelProvider labelProvider) {
		this.activeTabSource = activeTabSource;
		this.labelProvider = labelProvider;
		setText("Expand all");
		setToolTipText("Display fully qualified names");
		setDisabledImageDescriptor(imageSource.createImageDescriptor("togglefullyqualifieddisabled.gif"));
		setHoverImageDescriptor(imageSource.createImageDescriptor("togglefullyqualifiedenabled.gif"));
		setImageDescriptor(imageSource.createImageDescriptor("togglefullyqualifiedenabled.gif"));
		updateChecked();
	}

	public void run() {
		labelProvider.setFullyQualified(!labelProvider.isFullyQualified());
		updateChecked();
		activeTabSource.getActiveTab().refresh();
	}

	private void updateChecked() {
		setChecked(labelProvider.isFullyQualified());
	}

}
