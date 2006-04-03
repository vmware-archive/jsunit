package net.jsunit.action;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Result;
import net.jsunit.results.Skin;
import net.jsunit.results.XsltTransformer;
import net.jsunit.utility.XmlUtility;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;

public class TransformedTestRunResult implements Result {

    public void execute(ActionInvocation invocation) throws Exception {
        XmlProducer producer = (XmlProducer) invocation.getAction();
        String xml = XmlUtility.asString(producer.getXmlRenderable().asXml());

        SkinAware skinAware = (SkinAware) invocation.getAction();
        Skin skin = skinAware.getSkin();
        FileInputStream inputStream = new FileInputStream(skin.getPath());

        HttpServletResponse response = ServletActionContext.getResponse();
        OutputStream out = response.getOutputStream();

        new XsltTransformer(inputStream, xml).writeToOuputStream(out);
        out.close();
    }

}
