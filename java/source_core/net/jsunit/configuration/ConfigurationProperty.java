package net.jsunit.configuration;

import java.io.File;

public enum ConfigurationProperty {

    PORT("port", "8080", true, true),
    RESOURCE_BASE("resourceBase", ".", true, false),
    LOGS_DIRECTORY("logsDirectory", "." + File.separator + "logs", true, true),
    URL("url", null, false, false),
    BROWSER_FILE_NAMES("browserFileNames", null, true, false),
    CLOSE_BROWSERS_AFTER_TEST_RUNS("closeBrowsersAfterTestRuns", "true", true, false),
    LOG_STATUS("logStatus", "true", true, true),
    TIMEOUT_SECONDS("timeoutSeconds", "60", true, true),
    REMOTE_MACHINE_URLS("remoteMachineURLs", null, false, true);

    private String name;
    private String defaultValue;
    private boolean requiredForServer;
    private boolean requiredForFarmServer;

    ConfigurationProperty(String name, String defaultValue, boolean requiredForServer, boolean requiredForFarmServer) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.requiredForServer = requiredForServer;
        this.requiredForFarmServer = requiredForFarmServer;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isRequiredForServer() {
        return requiredForServer;
    }

    public boolean isRequiredForFarmServer() {
        return requiredForFarmServer;
    }

}
