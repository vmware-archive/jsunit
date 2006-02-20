package net.jsunit.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import net.jsunit.BlowingUpRemoteRunnerHitter;
import net.jsunit.DummyConfigurationSource;
import net.jsunit.JsUnitFarmServer;
import net.jsunit.MockRemoteRunnerHitter;
import net.jsunit.Utility;
import net.jsunit.configuration.Configuration;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.Filter;

public class FarmServerConfigurationActionTest extends TestCase {

	private FarmServerConfigurationAction action;

	public void setUp() throws Exception {
		super.setUp();
		action = new FarmServerConfigurationAction();
		action.setFarmServer(new JsUnitFarmServer(new Configuration(new DummyConfigurationSource())));
	}
	
	public void testSimple() throws Exception {
		MockRemoteRunnerHitter mockHitter = new MockRemoteRunnerHitter();
		Configuration configuration1 = configuration1();
		Configuration configuration2 = configuration2();
		mockHitter.documents.add(new Document(configuration1.asXml()));
		mockHitter.documents.add(new Document(configuration2.asXml()));
		action.setRemoteRunnerHitter(mockHitter);
		action.execute();
		assertEquals(2, mockHitter.urlsPassed.size());
		String xml = Utility.asString(action.getXmlRenderable().asXml());
		assertEquals(
			"<remoteConfigurations>" +
				"<remoteConfiguration remoteMachineURL=\"" + DummyConfigurationSource.REMOTE_URL_1 + "\">" +
					Utility.asString(configuration1.asXml()) +
				"</remoteConfiguration>" +
				"<remoteConfiguration remoteMachineURL=\"" + DummyConfigurationSource.REMOTE_URL_2 + "\">" +
					Utility.asString(configuration2.asXml()) +
				"</remoteConfiguration>" +
			"</remoteConfigurations>",
			xml
		);
	}

	public void testCrashingRemoteURLs() throws Exception {
		action.setRemoteRunnerHitter(new BlowingUpRemoteRunnerHitter());
		action.execute();
		Element rootElement = action.getXmlRenderable().asXml();
		List<CDATA> stackTraceElements = getCDATAExceptionStackTracesUnder(rootElement);
		assertEquals(2, stackTraceElements.size());
		for (CDATA stackTraceElement : stackTraceElements)
			stackTraceElement.detach();

		String xml = Utility.asString(rootElement);
		assertEquals(
			"<remoteConfigurations>" +
				"<remoteConfiguration remoteMachineURL=\"" + DummyConfigurationSource.REMOTE_URL_1 + "\">" +
					"<configuration failedToConnect=\"true\" />" +
				"</remoteConfiguration>" +
				"<remoteConfiguration remoteMachineURL=\"" + DummyConfigurationSource.REMOTE_URL_2 + "\">" +
				"<configuration failedToConnect=\"true\" />" +
				"</remoteConfiguration>" +
			"</remoteConfigurations>",
			xml
		);
	}

	private List<CDATA> getCDATAExceptionStackTracesUnder(Element rootElement) {
		Iterator it = rootElement.getDescendants(new Filter() {
			public boolean matches(Object arg0) {
				return arg0 instanceof CDATA;
			}
		});
		List<CDATA> stackTraceElements = new ArrayList<CDATA>();
		while (it.hasNext()) {
			CDATA next = (CDATA) it.next();
			stackTraceElements.add(next);
		}
		return stackTraceElements;
	}

	private Configuration configuration2() {
		Configuration configuration2 = new Configuration(new DummyConfigurationSource() {
			public String url() {
				return "http://www.2.example.com";
			}
		});
		return configuration2;
	}

	private Configuration configuration1() {
		Configuration configuration1 = new Configuration(new DummyConfigurationSource() {
			public String url() {
				return "http://www.1.example.com";
			}
		});
		return configuration1;
	}
	
}
