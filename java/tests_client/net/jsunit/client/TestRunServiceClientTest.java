package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.model.*;
import net.jsunit.services.TestRunService;
import net.jsunit.services.TestRunServiceServiceLocator;
import net.jsunit.utility.FileUtility;
import org.apache.axis.client.Stub;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;

public class TestRunServiceClientTest extends TestCase {

    private DummyTestSuitePageWriter writer;
    public static final String TEST_SUITE_FILE_NAME = "mySuite.html";
    private String directory;

    protected void setUp() throws Exception {
        super.setUp();
        directory = String.valueOf(System.currentTimeMillis());
        writer = new DummyTestSuitePageWriter(directory, TEST_SUITE_FILE_NAME);
        writer.writeFiles();
    }

    public void testSimple() throws Exception {
        MockTestRunService mockService = new MockTestRunService();
        MockTestRunServiceServiceLocator mockLocator = new MockTestRunServiceServiceLocator(mockService);
        TestRunServiceClient client = new TestRunServiceClient("http://www.example.com/jsUnitService", mockLocator, "a username", "a password", new DefaultReferencedJsFileResolver());
        client.addBrowserSpec(new BrowserSpecification(PlatformType.MACINTOSH, BrowserType.FIREFOX));
        client.addBrowserSpec(new BrowserSpecification(PlatformType.LINUX, BrowserType.OPERA));
        client.send(FileUtility.jsUnitPath(), new File(directory, TEST_SUITE_FILE_NAME));
        assertEquals("http://www.example.com/jsUnitService", mockLocator.portAddressPassed.toString());
        assertEquals(2, mockService.pagesPassed.length);
        assertEquals(2, mockService.specsPassed.length);
    }

    public void testBadServiceURL() throws Exception {
        TestRunServiceClient client = new TestRunServiceClient("not a url", new TestRunServiceServiceLocator(), "username", "password", new DefaultReferencedJsFileResolver());
        try {
            client.send(FileUtility.jsUnitPath(), new File("foo"));
            fail();
        } catch (Exception e) {
        }
    }

    static class MockTestRunServiceServiceLocator extends TestRunServiceServiceLocator {
        private URL portAddressPassed;
        private MockTestRunService mockTestRunService;

        public MockTestRunServiceServiceLocator(MockTestRunService mockTestRunService) {
            this.mockTestRunService = mockTestRunService;
        }

        public TestRunService getTestRunService(URL portAddress) throws ServiceException {
            this.portAddressPassed = portAddress;
            return mockTestRunService;
        }
    }

    static class MockTestRunService extends Stub implements TestRunService {
        private TestPage[] pagesPassed;
        private BrowserSpecification[] specsPassed;

        public DistributedTestRunResult runTests(TestPage[] pages, BrowserSpecification[] browserSpecs) throws RemoteException {
            this.pagesPassed = pages;
            this.specsPassed = browserSpecs;
            return null;
        }
    }

    protected void tearDown() throws Exception {
        writer.removeFiles();
        super.tearDown();
    }

}
