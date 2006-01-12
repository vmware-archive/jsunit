package net.jsunit.action;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import net.jsunit.XmlRenderable;
import net.jsunit.Utility;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;
import org.jdom.Element;
import org.jdom.Document;

public class XmlResult implements Result {

    public void execute(ActionInvocation invocation) throws Exception {
        XmlProducer producer = (XmlProducer) invocation.getAction();
        XmlRenderable xmlRenderable = producer.getXmlRenderable();
        Element element = xmlRenderable.asXml();
        Document document = new Document(element);
        String xmlString = Utility.asString(document);
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/xml");
        OutputStream out = response.getOutputStream();
        out.write(xmlString.getBytes());
        out.close();
    }

}
