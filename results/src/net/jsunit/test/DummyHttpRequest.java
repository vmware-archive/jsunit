package net.jsunit.test;
import java.io.*;
import java.security.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
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
public class DummyHttpRequest implements HttpServletRequest {
	protected Map parametersToValues;
	public DummyHttpRequest(Map parametersToValues) {
		this.parametersToValues = parametersToValues;
	}
	public String getAuthType() {
		return null;
	}
	public Cookie[] getCookies() {
		return null;
	}
	public long getDateHeader(String name) {
		return 0;
	}
	public String getHeader(String name) {
		return null;
	}
	public Enumeration getHeaders(String name) {
		return null;
	}
	public Enumeration getHeaderNames() {
		return null;
	}
	public int getIntHeader(String name) {
		return 0;
	}
	public String getMethod() {
		return null;
	}
	public String getPathInfo() {
		return null;
	}
	public String getPathTranslated() {
		return null;
	}
	public String getContextPath() {
		return null;
	}
	public String getQueryString() {
		return null;
	}
	public String getRemoteUser() {
		return null;
	}
	public boolean isUserInRole(String role) {
		return false;
	}
	public Principal getUserPrincipal() {
		return null;
	}
	public String getRequestedSessionId() {
		return null;
	}
	public String getRequestURI() {
		return null;
	}
	public StringBuffer getRequestURL() {
		return null;
	}
	public String getServletPath() {
		return null;
	}
	public HttpSession getSession(boolean create) {
		return null;
	}
	public HttpSession getSession() {
		return null;
	}
	public boolean isRequestedSessionIdValid() {
		return false;
	}
	public boolean isRequestedSessionIdFromCookie() {
		return false;
	}
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}
	public Object getAttribute(String name) {
		return null;
	}
	public Enumeration getAttributeNames() {
		return null;
	}
	public String getCharacterEncoding() {
		return null;
	}
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
	}
	public int getContentLength() {
		return 0;
	}
	public String getContentType() {
		return null;
	}
	public ServletInputStream getInputStream() throws IOException {
		return null;
	}
	public String getParameter(String name) {
		Object object = parametersToValues.get(name);
		if (object instanceof String) return (String) object;
		if (object instanceof String[]) return ((String[]) object)[0];
		return null;
	}
	public Enumeration getParameterNames() {
		return null;
	}
	public String[] getParameterValues(String name) {
		Object object = parametersToValues.get(name);
		if (object instanceof String) return new String[] {(String) object};
		if (object instanceof String[]) return (String[]) object;
		return null;
	}
	public Map getParameterMap() {
		return null;
	}
	public String getProtocol() {
		return null;
	}
	public String getScheme() {
		return null;
	}
	public String getServerName() {
		return null;
	}
	public int getServerPort() {
		return 0;
	}
	public BufferedReader getReader() throws IOException {
		return null;
	}
	public String getRemoteAddr() {
		return "Dummy Remote Address";
	}
	public String getRemoteHost() {
		return null;
	}
	public void setAttribute(String name, Object o) {
	}
	public void removeAttribute(String name) {
	}
	public Locale getLocale() {
		return null;
	}
	public Enumeration getLocales() {
		return null;
	}
	public boolean isSecure() {
		return false;
	}
	public RequestDispatcher getRequestDispatcher(String path) {
		return null;
	}
	public String getRealPath(String path) {
		return null;
	}
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}
}
