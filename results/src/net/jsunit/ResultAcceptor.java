package net.jsunit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpServer;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.jetty.servlet.ServletHandler;
/**
 * @author Edward Hieatt
 * 
 * ***** BEGIN LICENSE BLOCK *****
   - Version: MPL 1.1/GPL 2.0/LGPL 2.1
   -
   - The contents of this file are subject to the Mozilla Public License Version
   - 1.1 (the "License"); you may not use this file except in compliance with
   - the License. You may obtain a copy of the License at
   - http://www.mozilla.org/MPL/
   -
   - Software distributed under the License is distributed on an "AS IS" basis,
   - WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
   - for the specific language governing rights and limitations under the
   - License.
   -
   - The Original Code is Edward Hieatt code.
   -
   - The Initial Developer of the Original Code is
   - Edward Hieatt, edward@jsunit.net.
   - Portions created by the Initial Developer are Copyright (C) 2003
   - the Initial Developer. All Rights Reserved.
   -
   - Author Edward Hieatt, edward@jsunit.net
   -
   - Alternatively, the contents of this file may be used under the terms of
   - either the GNU General Public License Version 2 or later (the "GPL"), or
   - the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
   - in which case the provisions of the GPL or the LGPL are applicable instead
   - of those above. If you wish to allow use of your version of this file only
   - under the terms of either the GPL or the LGPL, and not to allow others to
   - use your version of this file under the terms of the MPL, indicate your
   - decision by deleting the provisions above and replace them with the notice
   - and other provisions required by the LGPL or the GPL. If you do not delete
   - the provisions above, a recipient may use your version of this file under
   - the terms of any one of the MPL, the GPL or the LGPL.
   -
   - ***** END LICENSE BLOCK *****
   
   @author Edward Hieatt
 */
public class ResultAcceptor {
	private static ResultAcceptor instance;
	private List results = new ArrayList();
	public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_RESOURCE_BASE=".";
	public static HttpServer server;
    public static String RESOURCE_BASE="resourceBase";

    public static void main(String args[]) throws Exception {
		if (args.length > 0) {
			int port = Integer.parseInt(args[0]);
            String resourceBase = DEFAULT_RESOURCE_BASE;
            if (args.length>1)
                resourceBase=args[1];
			startServer(port, resourceBase);
		} else
			startServer();
	}
	public static void startServer() throws Exception {
		startServer(DEFAULT_PORT, Utility.resourceBaseFromProperties());
	}
	public static void startServer(int port, String resourceBase) throws Exception {
		server = new HttpServer();
		server.addListener(":" + port);
		HttpContext context = server.getContext("/jsunit");
		ServletHandler handler = new ServletHandler();
		handler.addServlet("JsUnitResultAcceptor", "/acceptor", ResultAcceptorServlet.class.getName());
		handler.addServlet("JsUnitResultDisplayer", "/displayer", ResultDisplayerServlet.class.getName());
		context.addHandler(handler);

        context.setResourceBase(resourceBase);
        context.addHandler(new ResourceHandler());

        server.addContext(context);
		server.start();
	}
	public static void stopServer() throws Exception {
		server.stop();
	}
	public static ResultAcceptor instance() {
		if (instance == null)
			instance = new ResultAcceptor();
		return instance;
	}
	public TestSuiteResult accept(HttpServletRequest request) {
		TestSuiteResult result = TestSuiteResult.fromRequest(request);
		TestSuiteResult existingResultWithSameId = findResultWithId(result.getId());
		if (existingResultWithSameId != null)
			results.remove(existingResultWithSameId);
		results.add(result);
		result.writeLog();
		return result;
	}
	public List getResults() {
		return results;
	}
	public void clearResults() {
		results.clear();
	}
	public TestSuiteResult findResultWithId(String id) {
		TestSuiteResult result = findResultWithIdInResultList(id);
		if (result == null)
			result = TestSuiteResult.findResultWithIdInResultLogs(id);
		return result;
	}
	private TestSuiteResult findResultWithIdInResultList(String id) {
		Iterator it = getResults().iterator();
		while (it.hasNext()) {
			TestSuiteResult result = (TestSuiteResult) it.next();
			if (result.hasId(id))
				return result;
		}
		return null;
	}
}
