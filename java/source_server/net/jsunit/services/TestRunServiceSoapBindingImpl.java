package net.jsunit.services;

import net.jsunit.*;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.TestPage;
import net.jsunit.server.RemoteRunSpecificationBuilder;
import net.jsunit.uploaded.UploadedTestPage;
import net.jsunit.uploaded.UploadedTestPageFactory;
import org.apache.axis.MessageContext;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;
import java.rmi.RemoteException;
import java.util.List;

public class TestRunServiceSoapBindingImpl implements TestRunService, ServiceLifecycle {
    private JsUnitAggregateServer server;
    private String username;
    private String password;

    public DistributedTestRunResult runTests(TestPage testPage) throws RemoteException {
        User user = server.authenticateUser(username, password);
        if (user == null)
            throw new AuthenticationException();
        UploadedTestPage uploadedTestPage = new UploadedTestPageFactory().fromTestPage(testPage);
        uploadedTestPage.write();
        RemoteRunSpecificationBuilder builder = new RemoteRunSpecificationBuilder();
        List<RemoteRunSpecification> specs =
                builder.forAllBrowsersFromRemoteConfigurations(server.getAllRemoteMachineConfigurations());
        DistributedTestRunManager manager = new DistributedTestRunManager(
                server.getHitter(), server.getConfiguration(), uploadedTestPage.getURL(server.getConfiguration()), specs
        );
        manager.runTests();
        return manager.getDistributedTestRunResult();
    }

    public void init(Object context) throws ServiceException {
        MessageContext messageContext = (MessageContext) ((ServletEndpointContext) context).getMessageContext();
        username = messageContext.getUsername();
        password = messageContext.getPassword();
        //TODO: somehow inject the aggregate server - keep it in the context maybe?
        setAggregateServer((JsUnitAggregateServer) ServerRegistry.getServer());
    }

    public void setAggregateServer(JsUnitAggregateServer aggregateServer) {
        server = aggregateServer;
    }

    public void destroy() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public JsUnitAggregateServer getServer() {
        return server;
    }
}
