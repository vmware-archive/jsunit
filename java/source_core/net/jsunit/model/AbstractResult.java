package net.jsunit.model;

import java.util.List;

public abstract class AbstractResult implements Result {

    public ResultType getResultType() {
        ResultType worstResultType = ResultType.SUCCESS;
        for (Result childResult : getChildren()) {
            ResultType childResultType = childResult.getResultType();
            if (childResultType.isWorseThan(worstResultType))
                worstResultType = childResultType;
        }
        return worstResultType;
    }

    public int getFailureCount() {
        int failureCount = 0;
        for (Result childResult : getChildren())
            failureCount += childResult.getFailureCount();
        return failureCount;
    }

    public int getErrorCount() {
        int errorCount = 0;
        for (Result childResult : getChildren())
            errorCount += childResult.getErrorCount();
        return errorCount;
    }

    public int getTestCount() {
        int result = 0;
        for (Result childResult : getChildren())
            result += childResult.getTestCount();
        return result;
    }

    public boolean wasSuccessful() {
        return getResultType() == ResultType.SUCCESS;
    }

    protected abstract List<? extends Result> getChildren();

    public String displayString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("The test run had ");
        buffer.append(getErrorCount());
        buffer.append(" error(s) and ");
        buffer.append(getFailureCount());
        buffer.append(" failure(s).");
        return buffer.toString();
    }

    public final void addErrorStringTo(StringBuffer buffer) {
        if (wasSuccessful())
            return;
        addMyErrorStringTo(buffer);
        if (hasChildren()) {
            boolean isFirstProblem = true;
            for (Result result : getChildren()) {
                if (!result.wasSuccessful()) {
                    if (!isFirstProblem)
                        buffer.append("\n");
                    result.addErrorStringTo(buffer);
                    isFirstProblem = false;
                }
            }
        }
    }

    private boolean hasChildren() {
        return getChildren() != null && !getChildren().isEmpty();
    }

    protected void addMyErrorStringTo(StringBuffer buffer) {
    }

}
