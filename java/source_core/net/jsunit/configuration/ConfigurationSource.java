package net.jsunit.configuration;

public interface ConfigurationSource {

    String resourceBase();
    String port();
    String logsDirectory();
    String browserFileNames();
    String url();
    String closeBrowsersAfterTestRuns();
    
}