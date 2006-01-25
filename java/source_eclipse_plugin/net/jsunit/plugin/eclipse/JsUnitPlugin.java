package net.jsunit.plugin.eclipse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.jsunit.plugin.eclipse.launching.JsUnitLaunchConfiguration;
import net.jsunit.plugin.eclipse.preference.JsUnitPreferenceStore;
import net.jsunit.plugin.eclipse.resultsui.JsUnitTestResultsViewPart;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class JsUnitPlugin extends AbstractUIPlugin implements ILaunchListener {

	public static final String PLUGIN_ID = "net.jsunit";
	
	private static JsUnitPlugin soleInstance;

	private ResourceBundle resourceBundle;
	private URL iconBaseURL;
	private JsUnitTestResultsViewPart testRunViewPart;

	public JsUnitPlugin() {
		super();
		soleInstance = this;
		try {
			resourceBundle = ResourceBundle.getBundle("net.jsunit.eclipse.JsUnitPluginResources");
		} catch (MissingResourceException mre) {
		}
		try {
			iconBaseURL = new URL(Platform.getBundle(PLUGIN_ID).getEntry("/"), "icons/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected void initializeDefaultPreferences(IPreferenceStore store) {
	}

	public void start(BundleContext context) throws Exception {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		launchManager.addLaunchListener(this);
		getDisplay().syncExec(new Runnable() {
			public void run() {
				testRunViewPart = findTestRunnerViewPartInActivePage();
			}
		});
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		launchManager.removeLaunchListener(this);
		super.stop(context);
	}

	public static JsUnitPlugin soleInstance() {
		return soleInstance;
	}

	public static String getResourceString(String key) {
		ResourceBundle bundle = soleInstance.getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	private ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public static URL iconFileURL(String fileName) {
		try {
			return new URL(soleInstance.iconBaseURL, fileName);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public void launchRemoved(ILaunch launch) {
	}

	public void launchAdded(final ILaunch launch) {
		final ILaunchConfiguration launchConfiguration = launch.getLaunchConfiguration();
		boolean isJsUnitLaunch = isJsUnitLaunch(launchConfiguration);
		if (isJsUnitLaunch) {
			getDisplay().syncExec(new Runnable() {
				public void run() {
					testRunViewPart = showTestRunPart();
					int port;
					try {
						port = launchConfiguration.getAttribute(JsUnitLaunchConfiguration.ATTRIBUTE_PORT, -1);
					} catch (CoreException e) {
						throw new RuntimeException(e);
					}
					testRunViewPart.connectToRemoteRunner(port);
				}
			});
			testRunViewPart.reset();
		}
	}

	private boolean isJsUnitLaunch(ILaunchConfiguration launchConfiguration) {
		String testPageAttribute;
		try {
			testPageAttribute = launchConfiguration.getAttribute(
				JsUnitLaunchConfiguration.ATTRIBUTE_TEST_PAGE_PATH,
				(String) null);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
		return testPageAttribute != null;
	}

	private JsUnitTestResultsViewPart findTestRunnerViewPartInActivePage() {
		IWorkbenchPage activePage = activePage();
		if (activePage == null)
			return null;
		return (JsUnitTestResultsViewPart) activePage.findView(JsUnitTestResultsViewPart.ID);
	}

	public static Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

	private JsUnitTestResultsViewPart showTestRunPart() {
		try {
			return (JsUnitTestResultsViewPart) activePage().showView(JsUnitTestResultsViewPart.ID);
		} catch (PartInitException e) {
			throw new RuntimeException(e);
		}
	}

	private IWorkbenchPage activePage() {
		IWorkbenchWindow activeWorkbenchWindow = getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null)
			return null;
		return activeWorkbenchWindow.getActivePage();
	}

	public JsUnitTestResultsViewPart getTestRunViewPart() {
		return testRunViewPart;
	}

	public void launchChanged(ILaunch launch) {
	}

	public JsUnitPreferenceStore getJsUnitPreferenceStore() {
		return new JsUnitPreferenceStore(getPreferenceStore());
	}

	public static Image createImage(String imageName) {
		return createImageDescriptor(imageName).createImage();
	}
	
	public static ImageDescriptor createImageDescriptor(String imageName) {
		URL iconFileURL = iconFileURL(imageName);
		return ImageDescriptor.createFromURL(iconFileURL);
	}

}