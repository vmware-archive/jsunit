package net.jsunit;
import java.util.*;
import javax.servlet.http.*;
import org.mortbay.http.*;
import org.mortbay.jetty.servlet.*;
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
public class JsUnitResultAcceptor {
	protected static JsUnitResultAcceptor instance;
	protected List results = new ArrayList();
	public static final int DEFAULT_PORT = 8080;
	public static void main(String args[]) throws Exception {
		HttpServer server = new HttpServer();
		int port = DEFAULT_PORT;
		if (args.length > 0)
			port = Integer.parseInt(args[0]);
		server.addListener(":" + port);
		HttpContext context = server.getContext("/");
		ServletHandler handler = new ServletHandler();
		handler.addServlet("JsUnitResultAcceptor", "/jsunit/acceptor", JsUnitResultAcceptorServlet.class.getName());
		handler.addServlet("JsUnitResultDisplayer", "/jsunit/displayer", JsUnitResultDisplayerServlet.class.getName());
		context.addHandler(handler);
		server.start();
	}
	public static JsUnitResultAcceptor instance() {
		if (instance == null)
			instance = new JsUnitResultAcceptor();
		return instance;
	}
	public JsUnitTestSuiteResult accept(HttpServletRequest request) {
		JsUnitTestSuiteResult result = JsUnitTestSuiteResult.fromRequest(request);
		results.add(result);
		return result;
	}
	public List getResults() {
		return results;
	}
	public void clearResults() {
		results.clear();
	}
	public String writeResultWithId(String id) {
		JsUnitTestSuiteResult result = findResultWithId(id);
		if (result == null) {
			return "<error>No Test Result has been submitted with id " + id + "</error>";
		} else
			return result.writeXml();
	}
	public JsUnitTestSuiteResult findResultWithId(String id) {
		Iterator it = getResults().iterator();
		while (it.hasNext()) {
			JsUnitTestSuiteResult result = (JsUnitTestSuiteResult) it.next();
			if (result.hasId(id))
				return result;
		}
		return null;
	}
}
