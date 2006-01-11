package net.jsunit.plugin.eclipse.launching;

import junit.framework.TestCase;
import net.jsunit.JsUnitServer;
import net.jsunit.StandaloneTest;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.plugin.eclipse.DummyPreferenceStore;
import net.jsunit.plugin.eclipse.MockBrowserTestRunListener;
import net.jsunit.plugin.eclipse.MockErrorMessageRenderer;
import net.jsunit.plugin.eclipse.MockFile;
import net.jsunit.plugin.eclipse.MockStandaloneTestRunner;
import net.jsunit.plugin.eclipse.launching.JsUnitLauncher;
import net.jsunit.plugin.eclipse.preference.JsUnitPreferenceStore;
import net.jsunit.plugin.eclipse.preference.PreferenceConfigurationSource;
import net.jsunit.plugin.eclipse.preference.PreferenceConstants;

import org.eclipse.core.resources.IFile;

public class JsUnitLauncherTest extends TestCase {

	private JsUnitLauncher launcher;
	private MockStandaloneTestRunner mockStandaloneTestRunner;
	private DummyPreferenceStore preferenceStore;
	private MockErrorMessageRenderer mockErrorMessageRenderer;
	private MockBrowserTestRunListener mockBrowserTestRunListener;
	private IFile testPage;

	public void setUp() throws Exception {
		super.setUp();
		mockStandaloneTestRunner = new MockStandaloneTestRunner();
		preferenceStore = new DummyPreferenceStore();
		mockErrorMessageRenderer = new MockErrorMessageRenderer();
		mockBrowserTestRunListener = new MockBrowserTestRunListener();
		launcher = new JsUnitLauncher(
			new JsUnitPreferenceStore(preferenceStore), mockStandaloneTestRunner, mockErrorMessageRenderer, mockBrowserTestRunListener
		);
		testPage = new MockFile("c:\\mytests\\myTestSuite.html");
		setValidPreferences();
	}
	
	public void testInitialConditions() {
		assertNoJsUnitEnvironmentVariableIsSet();
		assertNull(mockStandaloneTestRunner.test);
		assertNull(mockErrorMessageRenderer.title);
		assertNull(mockErrorMessageRenderer.message);
		assertFalse(mockBrowserTestRunListener.testRunStartedCalled);
		assertFalse(mockBrowserTestRunListener.testRunFinishedCalled);
	}

	private void assertNoJsUnitEnvironmentVariableIsSet() {
		assertNull(System.getProperty(ConfigurationSource.RESOURCE_BASE));
		assertNull(System.getProperty(ConfigurationSource.BROWSER_FILE_NAMES));
		assertNull(System.getProperty(ConfigurationSource.URL));
		assertNull(System.getProperty(ConfigurationSource.PORT));
		assertNull(System.getProperty(ConfigurationSource.LOGS_DIRECTORY));
		assertNull(System.getProperty(ConfigurationSource.CLOSE_BROWSERS_AFTER_TEST_RUNS));
	}
	
	public void testValid() {
		launcher.launch(testPage);
		StandaloneTest test = mockStandaloneTestRunner.test;
		JsUnitServer server = (JsUnitServer) test.getBrowserTestRunner();
		PreferenceConfigurationSource source = (PreferenceConfigurationSource) server.getConfiguration().getSource();
		assertEquals("c:\\jsunit", source.resourceBase());
		assertEquals("c:\\Program Files\\Internet Explorer\\iexplore.exe", source.browserFileNames());
		assertEquals(
			"file:///c:\\jsunit"+java.io.File.separator+"testRunner.html?testPage=c:\\mytests\\myTestSuite.html&autoRun=true&submitresults=http://localhost:1234/jsunit/acceptor",
			source.url()
		);
		assertNull(mockErrorMessageRenderer.title);
		assertNull(mockErrorMessageRenderer.message);
		assertTrue(mockBrowserTestRunListener.testRunFinishedCalled);
	}

	public void testNoInstallationDirectory() {
		preferenceStore.setValue(PreferenceConstants.PREFERENCE_INSTALLATION_DIRECTORY, "");
		launcher.launch(testPage);
		assertNoJsUnitEnvironmentVariableIsSet();
		assertNull(mockStandaloneTestRunner.test);
		assertNotNull(mockErrorMessageRenderer.title);
		assertNotNull(mockErrorMessageRenderer.message);
		assertFalse(mockBrowserTestRunListener.testRunStartedCalled);
		assertFalse(mockBrowserTestRunListener.testRunFinishedCalled);
	}
	
	public void testNoBrowserFileNames() {
		preferenceStore.setValue(PreferenceConstants.PREFERENCE_BROWSER_FILE_NAMES, "");
		launcher.launch(testPage);
		assertNoJsUnitEnvironmentVariableIsSet();
		assertNull(mockStandaloneTestRunner.test);
		assertNotNull(mockErrorMessageRenderer.title);
		assertNotNull(mockErrorMessageRenderer.message);
		assertFalse(mockBrowserTestRunListener.testRunStartedCalled);
		assertFalse(mockBrowserTestRunListener.testRunFinishedCalled);
	}
	
	public void testBadPort() {
		preferenceStore.setValue(PreferenceConstants.PREFERENCE_PORT, -1);
		launcher.launch(testPage);
		assertNoJsUnitEnvironmentVariableIsSet();
		assertNull(mockStandaloneTestRunner.test);
		assertNotNull(mockErrorMessageRenderer.title);
		assertNotNull(mockErrorMessageRenderer.message);
		assertFalse(mockBrowserTestRunListener.testRunStartedCalled);
		assertFalse(mockBrowserTestRunListener.testRunFinishedCalled);
	}
	
	public void testBadFileExtension() {
		launcher.launch(new MockFile("myTestSuite.xyz"));
		assertNoJsUnitEnvironmentVariableIsSet();
		assertNull(mockStandaloneTestRunner.test);
		assertNotNull(mockErrorMessageRenderer.title);
		assertNotNull(mockErrorMessageRenderer.message);
		assertFalse(mockBrowserTestRunListener.testRunStartedCalled);
		assertFalse(mockBrowserTestRunListener.testRunFinishedCalled);
	}
	
	private void setValidPreferences() {
		preferenceStore.setValue(PreferenceConstants.PREFERENCE_INSTALLATION_DIRECTORY, "c:\\jsunit");
		preferenceStore.setValue(PreferenceConstants.PREFERENCE_LOGS_DIRECTORY, "c:\\jsunit\\logs");
		preferenceStore.setValue(PreferenceConstants.PREFERENCE_BROWSER_FILE_NAMES, "c:\\Program Files\\Internet Explorer\\iexplore.exe");
		preferenceStore.setValue(PreferenceConstants.PREFERENCE_PORT, 1234);
		preferenceStore.setValue(PreferenceConstants.PREFERENCE_CLOSE_BROWSERS_AFTER_TEST_RUNS, true);
		preferenceStore.setValue(PreferenceConstants.PREFERENCE_TEST_PAGE_EXTENSIONS, "html,htm");
	}
	
	public void tearDown() throws Exception {
		System.clearProperty(ConfigurationSource.RESOURCE_BASE);
		System.clearProperty(ConfigurationSource.BROWSER_FILE_NAMES);
		System.clearProperty(ConfigurationSource.URL);
		System.clearProperty(ConfigurationSource.PORT);
		System.clearProperty(ConfigurationSource.LOGS_DIRECTORY);
		System.clearProperty(ConfigurationSource.CLOSE_BROWSERS_AFTER_TEST_RUNS);
		super.tearDown();
	}
	
}