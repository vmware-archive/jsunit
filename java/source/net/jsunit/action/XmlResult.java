package net.jsunit.action;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;
import net.jsunit.Utility;
import net.jsunit.XmlRenderable;
import org.jdom.Document;
import org.jdom.Element;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

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
