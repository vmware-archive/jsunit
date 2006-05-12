package net.jsunit.services;

import net.jsunit.model.TestPage;
import net.jsunit.model.DistributedTestRunResult;

public interface TestRunService extends java.rmi.Remote {

    public DistributedTestRunResult runTests(TestPage page) throws java.rmi.RemoteException;

}
