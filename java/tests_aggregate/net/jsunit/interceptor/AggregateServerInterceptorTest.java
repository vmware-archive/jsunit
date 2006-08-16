package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.action.JsUnitAggregateServerAware;
import net.jsunit.configuration.AggregateConfiguration;
import net.jsunit.configuration.DummyConfigurationSource;

public class AggregateServerInterceptorTest extends TestCase {

    public void testSimple() throws Exception {
        MockAction action = new MockAction();
        JsUnitAggregateServer server = new JsUnitAggregateServer(new AggregateConfiguration(new DummyConfigurationSource()));
        assertNull(action.aggregateServer);
        AggregateServerInterceptor interceptor = new AggregateServerInterceptor();

        MockActionInvocation mockInvocation = new MockActionInvocation(action);
        interceptor.intercept(mockInvocation);

        assertSame(server, action.aggregateServer);
        assertTrue(mockInvocation.wasInvokeCalled);
    }

    static class MockAction implements Action, JsUnitAggregateServerAware {
        public JsUnitAggregateServer aggregateServer;

        public String execute() throws Exception {
            return null;
        }

        public void setAggregateServer(JsUnitAggregateServer aggregateServer) {
            this.aggregateServer = aggregateServer;
        }

        public JsUnitAggregateServer getAggregateServer() {
            return aggregateServer;
        }
    }

}
