package net.jsunit.services;

import net.jsunit.model.ServiceResult;
import net.jsunit.model.TestPage;

public interface TestRunService extends java.rmi.Remote {

    public ServiceResult runTests(TestPage page) throws java.rmi.RemoteException;

}
