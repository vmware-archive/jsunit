package net.jsunit.servlet;

import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserResultWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class BrowserResultDisplayerServlet extends JsUnitServlet {
 
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter(BrowserResultWriter.ID);
        String xml;
        if (id == null) {
            xml = "<error>No id specified</error>";
        } else {
            BrowserResult result = server.findResultWithId(id);
            if (result != null)
                xml = result.asXml();
            else
                xml = "<error>No Test Result has been submitted with id " + id + "</error>";
        }
        response.setContentType("text/xml");
        OutputStream out = response.getOutputStream();
        out.write(xml.getBytes());
        out.close();
    }
}
