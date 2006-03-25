package net.jsunit.action;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;

public class TestRunCountResult implements Result {

    public void execute(ActionInvocation invocation) throws Exception {
        TestRunCountAction action = (TestRunCountAction) invocation.getAction();
        long count = action.getTestRunCount();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/plain");
        OutputStream out = response.getOutputStream();
        BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
        bufferedOut.write(String.valueOf(count).getBytes());
        bufferedOut.close();
    }

}
