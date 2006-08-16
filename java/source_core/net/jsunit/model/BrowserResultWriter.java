package net.jsunit.model;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class BrowserResultWriter {

    public static final String
            ID = "id",
            BROWSER_RESULT = "browserResult",
            USER_AGENT = "userAgent",
            TIME = "time",
            TEST_CASE_RESULTS = "testCaseResults",
            JSUNIT_VERSION = "jsUnitVersion",
            REMOTE_ADDRESS = "remoteAddress",
            SERVER_SIDE_EXCEPTION_STACK_TRACE = "serverSideExceptionStackTrace",
            PROPERTIES = "properties",
            PROPERTY = "property",
            PROPERTY_KEY = "name",
            PROPERTY_VALUE = "value",
            URL = "url",
            TEST_PAGE_NAME = "testPage";

    BrowserResult browserResult;

    public BrowserResultWriter(BrowserResult result) {
        this.browserResult = result;
    }

    public String writeXmlString() {
        Element root = writeXml();
        Document document = new Document(root);
        return new XMLOutputter().outputString(document);
    }

    public String writeXmlFragment() {
        return new XMLOutputter().outputString(writeXml());
    }

    private void addPropertiesElementTo(Element element) {
        Element properties = new Element(PROPERTIES);
        element.addContent(properties);

        if (browserResult.completedTestRun()) {
            addProperty(properties, JSUNIT_VERSION, browserResult.getJsUnitVersion());
            addProperty(properties, USER_AGENT, browserResult.getUserAgent());
            addProperty(properties, REMOTE_ADDRESS, browserResult.getRemoteAddress());
            addProperty(properties, URL, browserResult.getBaseURL());
            addProperty(properties, TEST_PAGE_NAME, browserResult.getTestPageName());
        }
        if (browserResult.hasServerSideExceptionStackTrace()) {
            Element stackTrace = createPropertyElement(SERVER_SIDE_EXCEPTION_STACK_TRACE);
            stackTrace.addContent(new CDATA(browserResult.getServerSideExceptionStackTrace()));
            properties.addContent(stackTrace);
        }
    }

    private void addProperty(Element parent, String name, String value) {
        Element property = createPropertyElement(name);
        property.setAttribute(PROPERTY_VALUE, value == null ? "" : value);
        parent.addContent(property);
    }

    private Element createPropertyElement(String name) {
        Element property = new Element(PROPERTY);
        property.setAttribute(PROPERTY_KEY, name);
        return property;
    }

    private void addTestCasesElementTo(Element element) {
        Element testCasesElement = new Element(TEST_CASE_RESULTS);
        for (TestCaseResult result : browserResult._getTestCaseResults()) {
            new TestCaseResultWriter(result).addXmlTo(testCasesElement);
        }
        element.addContent(testCasesElement);
    }

    public String writeProblems() {
        StringBuffer buffer = new StringBuffer();
        for (TestCaseResult result : browserResult._getTestCaseResults()) {
            if (!result.wasSuccessful()) {
                if (buffer.length() > 0)
                    buffer.append("\n");
                String problemMessage = result.getProblemSummary(true);
                buffer.append(problemMessage);
            }
        }
        return buffer.toString();
    }

    public Element writeXml() {
        Element root = new Element(BROWSER_RESULT);
        root.setAttribute("type", browserResult._getResultType().name());
        Browser browser = browserResult.getBrowser();
        if (browser != null)
            root.addContent(browser.asXml());
        addPropertiesElementTo(root);
        if (browserResult.completedTestRun()) {
            root.setAttribute(ID, browserResult.getId());
            root.setAttribute(TIME, String.valueOf(browserResult.getTime()));
            addTestCasesElementTo(root);
        }
        return root;
    }
}
