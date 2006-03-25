package net.jsunit.interceptor;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.DummyHttpRequest;
import net.jsunit.SkinSource;
import net.jsunit.action.SkinAware;
import net.jsunit.results.Skin;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkinInterceptorTest extends TestCase {
    private Map<String, String[]> requestMap;

    public void setUp() throws Exception {
        super.setUp();
        requestMap = new HashMap<String, String[]>();
        HttpServletRequest request = new DummyHttpRequest(requestMap);
        ServletActionContext.setRequest(request);
    }

    public void tearDown() throws Exception {
        ServletActionContext.setRequest(null);
        super.tearDown();
    }

    public void testSimple() throws Exception {
        requestMap.put("skinId", new String[]{"4"});

        SkinInterceptor interceptor = new SkinInterceptor();
        MockAction action = new MockAction();
        MockActionInvocation invocation = new MockActionInvocation(action);
        interceptor.intercept(invocation);
        assertEquals(4, action.getSkin().getId());
        assertTrue(invocation.wasInvokeCalled);
    }

    public void testNoId() throws Exception {
        SkinInterceptor interceptor = new SkinInterceptor();
        MockAction action = new MockAction();
        MockActionInvocation invocation = new MockActionInvocation(action);
        interceptor.intercept(invocation);
        assertNull(action.getSkin());
        assertTrue(invocation.wasInvokeCalled);
    }

    static class MockAction implements Action, SkinAware {

        private Skin skin;

        public String execute() throws Exception {
            return null;
        }

        public void setSkin(Skin skin) {
            this.skin = skin;
        }

        public Skin getSkin() {
            return skin;
        }

        public SkinSource getSkinSource() {
            return new SkinSource() {
                public List<Skin> getSkins() {
                    return null;
                }

                public Skin getSkinById(int skinId) {
                    return skinId == 4 ? new Skin(4, new File("foo")) : null;
                }
            };
        }
    }

}
