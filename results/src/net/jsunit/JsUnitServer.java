package net.jsunit;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.jsunit.servlet.ResultAcceptorServlet;
import net.jsunit.servlet.ResultDisplayerServlet;
import net.jsunit.servlet.TestRunnerServlet;

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
public class JsUnitServer {
	private static Properties properties;
	private static JsUnitServer instance;
	private List results = new ArrayList();
	public static String PROPERTY_PORT = "port";
	public static String PROPERTY_RESOURCE_BASE = "resourceBase";
	public static String PROPERTY_LOGS_DIRECTORY = "logsDirectory";
	public static String PROPERTIES_FILE_NAME = "jsunit.properties";
	public static final int DEFAULT_PORT = 8080;
	public static final String DEFAULT_RESOURCE_BASE = ".";
	public static HttpServer server;
	private JsUnitServer() {
	}
	public static void main(String args[]) throws Exception {
		JsUnitServer.instance().startServer();
	}
	public void startServer() throws Exception {
		if (server == null) {			
			server = new HttpServer();
			server.addListener(":" + port());
			HttpContext context = server.getContext("/jsunit");
			ServletHandler handler = new ServletHandler();
			handler.addServlet("JsUnitResultAcceptor", "/acceptor", ResultAcceptorServlet.class.getName());
			handler.addServlet("JsUnitResultDisplayer", "/displayer", ResultDisplayerServlet.class.getName());
			handler.addServlet("JsUnitTestRunner", "/runner", TestRunnerServlet.class.getName());
			context.addHandler(handler);
			context.setResourceBase(resourceBase());
			context.addHandler(new ResourceHandler());
			server.addContext(context);
			server.start();
		}
	}
	public static void stopServer() throws Exception {
		server.stop();
	}
	public static JsUnitServer instance() {
		if (instance == null)
			instance = new JsUnitServer();
		return instance;
	}
	public TestSuiteResult accept(HttpServletRequest request) {
		Utility.log("Accepting result from " + request.getRemoteAddr() + "...");
		TestSuiteResult result = TestSuiteResult.fromRequest(request);
		TestSuiteResult existingResultWithSameId = findResultWithId(result.getId());
		if (existingResultWithSameId != null)
			results.remove(existingResultWithSameId);
		results.add(result);
		result.writeLog();
		Utility.log("...Done");
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
	public Properties propertiesFromFileName(String fileName) {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(new FileInputStream(fileName));
			} catch (Exception e) {
				throw new RuntimeException("Could not load " + fileName);
			}
		}
		return properties;
	}
	public Properties jsUnitProperties() {
		return propertiesFromFileName(PROPERTIES_FILE_NAME);
	}
	public String resourceBase() {
		String result = jsUnitProperties().getProperty(PROPERTY_RESOURCE_BASE);
		if (Utility.isEmpty(result))
			result = JsUnitServer.DEFAULT_RESOURCE_BASE;
		return result;
	}
	public String logsDirectory() {
		String result = jsUnitProperties().getProperty(PROPERTY_LOGS_DIRECTORY);
		if (Utility.isEmpty(result))
			result = resourceBase() + File.separator + "results" + File.separator + "logs";
		return result;
	}
	public int port() {
		int result;
		String portString = jsUnitProperties().getProperty(PROPERTY_PORT);
		if (Utility.isEmpty(portString))
			result = DEFAULT_PORT;
		else
			result = Integer.parseInt(portString);
		return result;
	}
}
