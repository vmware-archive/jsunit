package net.jsunit.action;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;

public class StatusResult implements Result {

    public void execute(ActionInvocation invocation) throws Exception {
        ServerStatusAction action = (ServerStatusAction) invocation.getAction();
        String statusMessage = action.getStatus();

        HttpServletResponse response = ServletActionContext.getResponse();
        OutputStream out = response.getOutputStream();
        BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
        bufferedOut.write(statusMessage.getBytes());
        bufferedOut.close();
    }

}
