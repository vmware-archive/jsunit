package net.jsunit.action;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import net.jsunit.XmlRenderable;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;

public class XmlResult implements Result {

	public void execute(ActionInvocation invocation) throws Exception {
		XmlProducer producer = (XmlProducer) invocation.getAction();
		XmlRenderable xmlRenderable = producer.getXmlRenderable();
        String xml = xmlRenderable.asXml();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/xml");
        OutputStream out = response.getOutputStream();
        out.write(xml.getBytes());
        out.close();
	}
	
}
