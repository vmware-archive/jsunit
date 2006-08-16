package net.jsunit;

import net.jsunit.model.DistributedTestRunResult;

import java.util.Date;

public interface DistributedTestRunListener {
    void notifyRunComplete(DistributedTestRunResult distributedTestRunResult, Date startDate, long millisTaken);
}
