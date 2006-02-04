package net.jsunit.model;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */
 
public class BrowserResultWriter {
	
    public static final String
	    ID = "id",
	    BROWSER_FILE_NAME = "browserFileName",
	    USER_AGENT = "userAgent",
	    TIME = "time",
	    TEST_CASES = "testCases",
	    TEST_CASE = "testCase",
	    TIMED_OUT = "timedOut",
	    FAILED_TO_LAUNCH = "failedToLaunch",
	    JSUNIT_VERSION = "jsUnitVersion",
	    REMOTE_ADDRESS = "remoteAddress",
	    PROPERTIES = "properties",
	    PROPERTY = "property",
	    PROPERTY_KEY = "name",
	    PROPERTY_VALUE = "value",
	    BASE_URL = "baseURL";

    BrowserResult browserResult;

    public BrowserResultWriter(BrowserResult result) {
        this.browserResult = result;
    }

    public String writeXml() {
        Element root = createRootElement();
        Document document = new Document(root);
        return new XMLOutputter().outputString(document);
    }

    public String writeXmlFragment() {
        return new XMLOutputter().outputString(createRootElement());
    }

    private Element createRootElement() {
        Element root = new Element("browserResult");
        if (browserResult.timedOut())
        	root.setAttribute(TIMED_OUT, String.valueOf(true));
        if (browserResult.failedToLaunch())
        	root.setAttribute(FAILED_TO_LAUNCH, String.valueOf(true));
        addPropertiesElementTo(root);
        if (browserResult.completedTestRun()) {
            root.setAttribute(ID, browserResult.getId());
            root.setAttribute(TIME, String.valueOf(browserResult.getTime()));
	        addTestCasesElementTo(root);
        }
        return root;
    }

    private void addPropertiesElementTo(Element element) {
        Element properties = new Element(PROPERTIES);
        element.addContent(properties);
        addProperty(properties, BROWSER_FILE_NAME, browserResult.getBrowserFileName());
        if (browserResult.completedTestRun()) {
	        addProperty(properties, JSUNIT_VERSION, browserResult.getJsUnitVersion());
	        addProperty(properties, USER_AGENT, browserResult.getUserAgent());
	        addProperty(properties, REMOTE_ADDRESS, browserResult.getRemoteAddress());
	        addProperty(properties, BASE_URL, browserResult.getBaseURL());
        }
    }

    private void addProperty(Element parent, String name, String value) {
        Element property = new Element(PROPERTY);
        property.setAttribute(PROPERTY_KEY, name);
        property.setAttribute(PROPERTY_VALUE, value == null ? "" : value);
        parent.addContent(property);
    }

    private void addTestCasesElementTo(Element element) {
    	Element testCasesElement = new Element(TEST_CASES);
        for (TestCaseResult result : browserResult.getTestCaseResults()) {
            new TestCaseResultWriter(result).addXmlTo(testCasesElement);
        }
        element.addContent(testCasesElement);
    }

    public String writeProblems() {
        StringBuffer buffer = new StringBuffer();
        for (TestCaseResult result : browserResult.getTestCaseResults()) {
            if (!result.wasSuccessful()) {
                if (buffer.length() > 0)
                    buffer.append("\n");
                String problemMessage = result.getProblemSummary();
                buffer.append(problemMessage);
            }
        }
        return buffer.toString();
    }

    public Element asXml() {
        return createRootElement();
    }
}
