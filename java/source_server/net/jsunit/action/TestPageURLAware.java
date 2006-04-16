package net.jsunit.action;

import net.jsunit.configuration.Configuration;

public interface TestPageURLAware {

    void setUrl(String url);

    Configuration getServerConfiguration();

}
