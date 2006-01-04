package net.jsunit.servlet;

import net.jsunit.Utility;
import net.jsunit.model.BrowserResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */ 

public class BrowserResultAcceptorServlet extends JsUnitServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utility.log("BrowserResultAcceptorServlet: Received request");
        BrowserResult result = BrowserResult.fromRequest(request);
        server.accept(result);
        String xml = result.asXml();
        response.setContentType("text/xml");
        OutputStream out = response.getOutputStream();
        out.write(xml.getBytes());
        out.close();
        Utility.log("BrowserResultAcceptorServlet: Done");
    }
}
