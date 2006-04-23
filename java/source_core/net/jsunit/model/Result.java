package net.jsunit.model;

import net.jsunit.XmlRenderable;

public interface Result extends XmlRenderable {

    public int getErrorCount();

    public int getFailureCount();

    public int getTestCount();

    public ResultType getResultType();

    public boolean wasSuccessful();

    public String displayString();

    public void addErrorStringTo(StringBuffer buffer);

}
