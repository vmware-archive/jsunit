package net.jsunit;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class TestCaseResultWriter {
	public static final String TEST_CASE = "testcase", NAME = "name", TIME = "time", FAILURE = "failure", ERROR = "error", MESSAGE = "message";
	
	private TestCaseResult result;
	public TestCaseResultWriter(TestCaseResult result) {
		this.result=result;
	}
	public void addXmlTo(Element element) {
		element.addContent(createTestCaseElement());
	}
	public Element createTestCaseElement() {
		Element testCaseElement = new Element(TEST_CASE);
		testCaseElement.setAttribute(NAME, result.getName());
		testCaseElement.setAttribute(TIME, String.valueOf(result.getTime()));
		if (result.hadFailure()) {
			Element failureElement = new Element(FAILURE);
			failureElement.setAttribute(MESSAGE, result.getFailure());
			testCaseElement.addContent(failureElement);
		} else if (result.hadError()) {
			Element errorElement = new Element(ERROR);
			errorElement.setAttribute(MESSAGE, result.getError());
			testCaseElement.addContent(errorElement);
		}
		return testCaseElement;
	}
	
	public String writeProblemSummary() {
		if (result.hadFailure())
			return result.getName() + " failed:\n" + result.getFailure();
		else if (result.hadError())
			return result.getName() + " had an error:\n" + result.getError();
		return null;
	}
	public String writeXmlFragment() {
		return new XMLOutputter().outputString(createTestCaseElement());
	}
}
