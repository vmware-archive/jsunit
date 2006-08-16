package net.jsunit.services;

import net.jsunit.model.BrowserSpecification;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.TestPage;

public interface TestRunService extends java.rmi.Remote {

    public DistributedTestRunResult runTests(TestPage[] pages, BrowserSpecification[] browserSpecs) throws java.rmi.RemoteException;

}
