package net.jsunit.services;

import net.jsunit.DistributedTestRunManager;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.ServerRegistry;
import net.jsunit.RemoteRunSpecification;
import net.jsunit.uploaded.UploadedTestPage;
import net.jsunit.uploaded.UploadedTestPageFactory;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.ServiceResult;
import net.jsunit.model.TestPage;
import net.jsunit.server.RemoteRunSpecificationBuilder;
import net.jsunit.utility.XmlUtility;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import java.rmi.RemoteException;
import java.util.List;

public class TestRunServiceSoapBindingImpl implements TestRunService, ServiceLifecycle {
    private JsUnitAggregateServer server;

    public ServiceResult runTests(TestPage testPage) throws RemoteException {
        UploadedTestPage uploadedTestPage = new UploadedTestPageFactory().fromTestPage(testPage);
        uploadedTestPage.write();
        RemoteRunSpecificationBuilder builder = new RemoteRunSpecificationBuilder();
        List<RemoteRunSpecification> specs =
                builder.forAllBrowsersFromRemoteConfigurations(server.getAllRemoteMachineConfigurations());
        DistributedTestRunManager manager = new DistributedTestRunManager(
                server.getHitter(), server.getConfiguration(), uploadedTestPage.getURL(server.getConfiguration()), specs
        );
        manager.runTests();
        DistributedTestRunResult result = manager.getDistributedTestRunResult();
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setXml(XmlUtility.asString(result.asXml()));
        return serviceResult;
    }

    public void init(Object context) throws ServiceException {
        //TODO: somehow inject the aggregate server - keep it in the context maybe?
        setAggregateServer((JsUnitAggregateServer) ServerRegistry.getServer());
    }

    public void setAggregateServer(JsUnitAggregateServer aggregateServer) {
        server = aggregateServer;
    }

    public void destroy() {
    }
}
