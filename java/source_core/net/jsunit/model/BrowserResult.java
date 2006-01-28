package net.jsunit.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.jsunit.Utility;
import net.jsunit.XmlRenderable;

import org.jdom.Document;
import org.jdom.Element;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class BrowserResult implements XmlRenderable {
    private String remoteAddress, id, jsUnitVersion, userAgent, baseURL;
	private List<TestPageResult> testPageResults = new ArrayList<TestPageResult>();
    private double time;

    public BrowserResult() {
        this.id = String.valueOf(System.currentTimeMillis());
    }

    public static File logFileForId(File logsDirectory, String id) {
        return new File(logsDirectory+File.separator+ id + ".xml");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != null)
            this.id = id;
    }

    public boolean hasId(String id) {
        return this.id.equals(id);
    }

    public String getJsUnitVersion() {
        return jsUnitVersion;
    }

    public void setJsUnitVersion(String jsUnitVersion) {
        this.jsUnitVersion = jsUnitVersion;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public List<TestCaseResult> getTestCaseResults() {
    	List<TestCaseResult> result = new ArrayList<TestCaseResult>();
    	for (TestPageResult pageResult : getTestPageResults())
    		result.addAll(pageResult.getTestCaseResults());
        return result;
    }

    public void setTestCaseStrings(String[] testCaseResultStrings) {
        buildTestCaseResults(testCaseResultStrings);
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    private void buildTestCaseResults(String[] testCaseResultStrings) {
        if (testCaseResultStrings == null)
            return;
        for (String testCaseResultString : testCaseResultStrings)
            addTestCaseResult(TestCaseResult.fromString(testCaseResultString));
    }

    public static BrowserResult findResultWithIdInResultLogs(File logsDirectory, String id) {
        File logFile = logFileForId(logsDirectory, id);
        if (logFile.exists())
            return fromXmlFile(logFile);
        return null;
    }

    public int errorCount() {
        int result = 0;
        for (TestPageResult testPageResult : testPageResults)
            result += testPageResult.errorCount();
        return result;
    }

    public int failureCount() {
        int result = 0;
        for (TestPageResult testPageResult : testPageResults)
            result+=testPageResult.failureCount();
        return result;
    }

    public int count() {
        int result = 0;
        for (TestPageResult testPageResult : testPageResults)
            result+=testPageResult.count();
        return result;
    }

    public Element asXml() {
        return new BrowserResultWriter(this).asXml();
    }

    public String asXmlFragment() {
        return new BrowserResultWriter(this).writeXmlFragment();
    }

    public void writeLog(File logsDirectory) {
        writeXmlToFile(logsDirectory);
    }

    private void writeXmlToFile(File logsDirectory) {
        Element element = asXml();
        String string = Utility.asString(element);
        Utility.writeFile(string, logFileForId(logsDirectory, getId()));
    }

    public boolean wasSuccessful() {
        return getResultType() == ResultType.SUCCESS;
    }

    public static BrowserResult fromXmlFile(File aFile) {
        return new BrowserResultBuilder().build(aFile);
    }

    public void addTestCaseResult(TestCaseResult testCaseResult) {
        String testPageName = testCaseResult.getTestPageName();
		TestPageResult testPageResult = findTestPageResultForTestPageWithName(testPageName);
        if (testPageResult == null) {
        	testPageResult = new TestPageResult(testPageName);
        	testPageResults.add(testPageResult);
        }
        testPageResult.addTestCaseResult(testCaseResult);
    }

    private TestPageResult findTestPageResultForTestPageWithName(String testPageName) {
    	for (TestPageResult testPageResult : testPageResults)
    		if (testPageResult.getTestPageName().equals(testPageName))
    			return testPageResult;
		return null;
	}

	public ResultType getResultType() {
        if (errorCount() > 0)
            return ResultType.ERROR;
        if (failureCount() > 0)
            return ResultType.FAILURE;
        return ResultType.SUCCESS;
    }

	public Document asXmlDocument() {
		return new Document(asXml());
	}

	public List<TestPageResult> getTestPageResults() {
		return testPageResults;
	}

}
