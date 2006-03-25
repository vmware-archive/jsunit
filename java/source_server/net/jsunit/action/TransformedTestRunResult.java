package net.jsunit.action;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;
import net.jsunit.results.Skin;
import net.jsunit.utility.XmlUtility;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.StringReader;

public class TransformedTestRunResult implements Result {

    public void execute(ActionInvocation invocation) throws Exception {
        XmlProducer producer = (XmlProducer) invocation.getAction();
        String xml = XmlUtility.asString(producer.getXmlRenderable().asXml());

        SkinAware skinAware = (SkinAware) invocation.getAction();
        Skin skin = skinAware.getSkin();
        TransformerFactory factory = TransformerFactory.newInstance();

        Source xslSource = new StreamSource(new FileInputStream(skin.getPath()));
        Templates templates = factory.newTemplates(xslSource);
        Transformer transformer = templates.newTransformer();

        Source xmlSource = new StreamSource(new StringReader(xml));

        HttpServletResponse response = ServletActionContext.getResponse();
        OutputStream out = response.getOutputStream();
        transformer.transform(xmlSource, new StreamResult(out));
        out.close();

    }
}
