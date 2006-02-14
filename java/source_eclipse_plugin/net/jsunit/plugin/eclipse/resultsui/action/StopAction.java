/**
 * 
 */
package net.jsunit.plugin.eclipse.resultsui.action;

import net.jsunit.plugin.eclipse.ImageSource;
import net.jsunit.plugin.eclipse.resultsui.TestRunStoppable;

import org.eclipse.jface.action.Action;

public class StopAction extends Action {
	
	private final TestRunStoppable stoppable;

	public StopAction(TestRunStoppable stoppable, ImageSource imageSource) {
		this.stoppable = stoppable;
		setText("Stop");
		setToolTipText("Stop current test run");
		setDisabledImageDescriptor(imageSource.createImageDescriptor("stopdisabled.gif"));
		setHoverImageDescriptor(imageSource.createImageDescriptor("stopenabled.gif"));
		setImageDescriptor(imageSource.createImageDescriptor("stopenabled.gif"));
	}

	public void run() {
		stoppable.stopTestRun();
		setEnabled(false);
	}

}