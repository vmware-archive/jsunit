package net.jsunit.plugin.eclipse;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class DefaultErrorMessageRenderer implements ErrorMessageRenderer {

	public void showError(final String title, final String message) {
		JsUnitPlugin.getDisplay().asyncExec(new Runnable() {
			public void run() {
				MessageDialog.openInformation(new Shell(), title, message);
			}
		});
	}

}
