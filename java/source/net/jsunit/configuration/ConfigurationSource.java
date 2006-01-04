package net.jsunit.configuration;

public interface ConfigurationSource {

    String PORT = "port";
    String RESOURCE_BASE = "resourceBase";
    String LOGS_DIRECTORY = "logsDirectory";
    String URL = "url";
    String BROWSER_FILE_NAMES = "browserFileNames";
    String CLOSE_BROWSERS_AFTER_TEST_RUNS = "closeBrowsersAfterTestRuns";
    
    String resourceBase();
    String port();
    String logsDirectory();
    String browserFileNames();
    String url();
    String closeBrowsersAfterTestRuns();
    
}