package net.jsunit.plugin.eclipse;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class DefaultErrorMessageRenderer implements ErrorMessageRenderer {

	public void showError(String title, String message) {
		MessageDialog.openInformation(new Shell(), title, message);
	}

}
