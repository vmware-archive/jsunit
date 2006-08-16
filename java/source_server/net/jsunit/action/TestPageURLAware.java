package net.jsunit.action;

import net.jsunit.configuration.Configuration;

public interface TestPageURLAware extends InvalidTestRunAttemptAware {

    void setUrl(String url);

    Configuration getConfiguration();

}
