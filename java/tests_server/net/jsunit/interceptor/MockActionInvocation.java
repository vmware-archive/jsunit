package net.jsunit.interceptor;

import com.opensymphony.xwork.*;
import com.opensymphony.xwork.interceptor.PreResultListener;
import com.opensymphony.xwork.util.OgnlValueStack;

import java.util.ArrayList;
import java.util.List;

public class MockActionInvocation implements ActionInvocation {

    private Object action;
    public boolean wasInvokeCalled;
    public List<PreResultListener> preResultListeners = new ArrayList<PreResultListener>();

    public MockActionInvocation(Object action) {
        this.action = action;
    }

    public Object getAction() {
        return action;
    }

    public boolean isExecuted() {
        return false;
    }

    public ActionContext getInvocationContext() {
        return null;
    }

    public ActionProxy getProxy() {
        return null;
    }

    public Result getResult() throws Exception {
        return null;
    }

    public String getResultCode() {
        return null;
    }

    public void setResultCode(String string) {
    }

    public OgnlValueStack getStack() {
        return null;
    }

    public void addPreResultListener(PreResultListener listener) {
        preResultListeners.add(listener);
    }

    public String invoke() throws Exception {
        wasInvokeCalled = true;
        if (action instanceof Action)
            return ((Action) action).execute();
        else
            return null;
    }

    public String invokeActionOnly() throws Exception {
        return null;
    }

}
