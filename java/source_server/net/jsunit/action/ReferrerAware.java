package net.jsunit.action;

import net.jsunit.configuration.Configuration;

public interface ReferrerAware {

    public void setReferrer(String referrer);

    public String getReferrer();

    public Configuration getConfiguration();
}
