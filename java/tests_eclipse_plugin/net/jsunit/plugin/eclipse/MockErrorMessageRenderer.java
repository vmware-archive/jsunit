package net.jsunit.plugin.eclipse;

public class MockErrorMessageRenderer implements ErrorMessageRenderer {

	public String title;
	public String message;

	public void showError(String title, String message) {
		this.title = title;
		this.message = message;

	}

}
