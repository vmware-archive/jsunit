package net.jsunit.action;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;
import net.jsunit.StatusMessage;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public class StatusResult implements Result {

    public void execute(ActionInvocation invocation) throws Exception {
        ServerStatusAction action = (ServerStatusAction) invocation.getAction();
        List<StatusMessage> messages = action.getStatusMessages();
        StringBuffer buffer = new StringBuffer();
        for (Iterator<StatusMessage> it = messages.iterator(); it.hasNext();) {
            StatusMessage message = it.next();
            buffer.append(new SimpleDateFormat().format(message.getDate()));
            buffer.append(": ");
            buffer.append(message.getMessage());
            if (it.hasNext())
                buffer.append("|");
        }

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/plain");
        OutputStream out = response.getOutputStream();
        BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
        bufferedOut.write(buffer.toString().getBytes());
        bufferedOut.close();
    }

}
