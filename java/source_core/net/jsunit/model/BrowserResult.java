package net.jsunit.model;

import net.jsunit.XmlRenderable;
import net.jsunit.utility.StringUtility;
import org.jdom.Document;
import org.jdom.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class BrowserResult extends AbstractResult implements XmlRenderable {

    private Browser browser;
    private String remoteAddress;
    private String id;
    private String jsUnitVersion;
    private String userAgent;
    private String baseURL;
    private double time;
    private List<TestPageResult> testPageResults = new ArrayList<TestPageResult>();
    private String serverSideExceptionStackTrace;
    private ResultType resultType;

    public BrowserResult() {
        this.id = String.valueOf(System.currentTimeMillis());
    }

    public Browser getBrowser() {
        return browser;
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

    public List<TestCaseResult> _getTestCaseResults() {
        List<TestCaseResult> result = new ArrayList<TestCaseResult>();
        for (TestPageResult pageResult : _getTestPageResults())
            result.addAll(pageResult._getTestCaseResults());
        return result;
    }

    public void _setTestCaseStrings(String[] testCaseResultStrings) {
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

    public Element asXml() {
        return new BrowserResultWriter(this).writeXml();
    }

    public String asXmlFragment() {
        return new BrowserResultWriter(this).writeXmlFragment();
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

    public ResultType _getResultType() {
        if (resultType == null)
            return super._getResultType();
        return resultType;
    }

    public Document asXmlDocument() {
        return new Document(asXml());
    }

    public List<TestPageResult> _getTestPageResults() {
        return testPageResults;
    }

    public TestPageResult[] getTestPageResults() {
        return testPageResults.toArray(new TestPageResult[testPageResults.size()]);
    }

    public void setTestPageResults(TestPageResult[] results) {
        testPageResults = Arrays.asList(results);
    }

    public String getDisplayString() {
        return _getResultType().getDisplayString();
    }

    public boolean completedTestRun() {
        return _getResultType().completedTestRun();
    }

    public boolean timedOut() {
        return _getResultType().timedOut();
    }

    public boolean failedToLaunch() {
        return _getResultType().failedToLaunch();
    }

    public void _setServerSideException(Throwable throwable) {
        serverSideExceptionStackTrace = StringUtility.stackTraceAsString(throwable);
    }

    public String getServerSideExceptionStackTrace() {
        return serverSideExceptionStackTrace;
    }

    public void setServerSideExceptionStackTrace(String serverSideExceptionStackTrace) {
        this.serverSideExceptionStackTrace = serverSideExceptionStackTrace;
    }

    public boolean hasServerSideExceptionStackTrace() {
        return serverSideExceptionStackTrace != null;
    }

    protected List<? extends Result> getChildren() {
        return testPageResults;
    }

    public void setBrowser(Browser browser) {
        this.browser = browser;
    }

    public boolean isForBrowser(Browser browser) {
        return this.browser.equals(browser);
    }

    public String getBrowserDisplayString() {
        return browser.getDisplayName();
    }

    protected void addMyErrorStringTo(StringBuffer buffer) {
        buffer.append(getBrowserDisplayString());
        buffer.append("\n");
    }

    public void _setResultType(ResultType type) {
        this.resultType = type;
    }

    public String getLogUrl(String baseURLString) {
        String path = "/jsunit/displayer?id=" + getId() + "&browserId=" + getBrowser().getId();
        try {
            URL baseURL = new URL(baseURLString);
            return new URL(baseURL.getProtocol(), baseURL.getHost(), baseURL.getPort(), path).toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
