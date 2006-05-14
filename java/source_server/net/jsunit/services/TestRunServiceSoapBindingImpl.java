package net.jsunit.services;

import net.jsunit.*;
import net.jsunit.model.BrowserSpecification;
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
import java.util.Arrays;
import java.util.List;

public class TestRunServiceSoapBindingImpl implements TestRunService, ServiceLifecycle {
    private JsUnitAggregateServer server;
    private String username;
    private String password;
    private String ipAddress;

    public DistributedTestRunResult runTests(TestPage testPage, BrowserSpecification[] browserSpecs) throws RemoteException {
        server.logStatus("Received SOAP request from " + ipAddress);
        User user = server.authenticateUser(username, password);
        if (user == null) {
            server.logStatus("Invalid username/password attempt: " + username + "/" + password);
            throw new AuthenticationException();
        }
        UploadedTestPage uploadedTestPage = new UploadedTestPageFactory().fromTestPage(testPage);
        uploadedTestPage.write();
        RemoteRunSpecificationBuilder builder = new RemoteRunSpecificationBuilder();
        List<RemoteRunSpecification> specs = null;
        try {
            specs = builder.forBrowserSpecifications(Arrays.asList(browserSpecs), server.getAllRemoteMachineConfigurations());
        } catch (InvalidBrowserSpecificationException e) {
            throw new RemoteException("Invalid browser specification", e);
        }
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
        ipAddress = (String) messageContext.getProperty("remoteaddr");
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
