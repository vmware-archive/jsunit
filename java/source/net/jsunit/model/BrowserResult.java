package net.jsunit.model;

import javax.servlet.http.HttpServletRequest;

import net.jsunit.Utility;
import net.jsunit.XmlRenderable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class BrowserResult implements XmlRenderable {
    private String remoteAddress, id, jsUnitVersion, userAgent, baseURL;
    private List<TestCaseResult> testCaseResults = new ArrayList<TestCaseResult>();
    private double time;
    private String SEPARATOR = "---------------------";

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
        return testCaseResults;
    }

    public void setTestCaseStrings(String[] testCaseResultStrings) {
        buildTestCaseResults(testCaseResultStrings);
    }

    public static BrowserResult fromRequest(HttpServletRequest request) {
        BrowserResult result = new BrowserResult();
        String testId = request.getParameter(BrowserResultWriter.ID);
        if (!Utility.isEmpty(testId))
            result.setId(testId);
        result.setRemoteAddress(request.getRemoteAddr());
        result.setUserAgent(request.getParameter(BrowserResultWriter.USER_AGENT));
        result.setBaseURL(request.getParameter(BrowserResultWriter.BASE_URL));
        String time = request.getParameter(BrowserResultWriter.TIME);
        if (!Utility.isEmpty(time))
            result.setTime(Double.parseDouble(time));
        result.setJsUnitVersion(request.getParameter(BrowserResultWriter.JSUNIT_VERSION));
        result.setTestCaseStrings(request.getParameterValues(BrowserResultWriter.TEST_CASES));
        return result;
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
            testCaseResults.add(TestCaseResult.fromString(testCaseResultString));
    }

    public static BrowserResult findResultWithIdInResultLogs(File logsDirectory, String id) {
        File logFile = logFileForId(logsDirectory, id);
        if (logFile.exists())
            return fromXmlFile(logFile);
        return null;
    }

    public int errorCount() {
        int result = 0;
        for (TestCaseResult testCaseResult : testCaseResults) {
            if (testCaseResult.hadError())
                result++;
        }
        return result;
    }

    public int failureCount() {
        int result = 0;
        for (TestCaseResult testCaseResult : testCaseResults) {
            if (testCaseResult.hadFailure())
                result++;
        }
        return result;
    }

    public int count() {
        return testCaseResults.size();
    }

    public Element asXml() {
        return new BrowserResultWriter(this).asXml();
    }

    public String asXmlFragment() {
        return new BrowserResultWriter(this).writeXmlFragment();
    }

    public void writeLog(File logsDirectory) {
        writeXmlToFile(logsDirectory);
        logProblems();
    }

    private void logProblems() {
        String problems = new BrowserResultWriter(this).writeProblems();
        if (!Utility.isEmpty(problems)) {
            Utility.log("Problems:");
            Utility.log(SEPARATOR, false);
            Utility.log(problems, false);
            Utility.log(SEPARATOR, false);
        }
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

    public void addTestCaseResult(TestCaseResult result) {
        testCaseResults.add(result);
    }

    public ResultType getResultType() {
        if (errorCount() > 0)
            return ResultType.ERROR;
        if (failureCount() > 0)
            return ResultType.FAILURE;
        return ResultType.SUCCESS;
    }

}
