<%@ page import="net.jsunit.JsUnitAggregateServer" %>
<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.configuration.RemoteConfiguration" %>
<%@ page import="net.jsunit.model.Browser" %>
<%@ page import="net.jsunit.results.Skin" %>
<%@ page import="java.util.List" %>
<%
    JsUnitServer server = ServerRegistry.getServer();
    boolean multipleBrowsersAllowed = Boolean.parseBoolean(request.getParameter("multipleBrowsersAllowed"));%>
<table width="50%">
    <%if (!server.isAggregateServer()) {%>
    <tr>
        <td width="10%" valign="top">
            <b>Browser<%if (multipleBrowsersAllowed) {%>s<%}%>:</b>
        </td>
        <td>
            <table width="100%">
                <%
                    List<Browser> browsers = server.getConfiguration().getBrowsers();
                    for (int i = 0; i < browsers.size(); i++) {
                        Browser browser = browsers.get(i);
                %>
                <tr>
                    <td valign="top" nowrap>
                        <input type="<%=multipleBrowsersAllowed ? "checkbox" : "radio"%>" name="browserId" value="<%=browser.getId()%>" <%if (multipleBrowsersAllowed || i == 0) {%> checked<%}%>
                                <%if (browser.getType() != null) {%>
                        <img src="<%=browser.getLogoPath()%>" alt="<%=browser.getDisplayName()%>" title="<%=browser.getDisplayName()%>">
                        <%}%>
                        <%=browser.getDisplayName()%>
                    </td>
                </tr>
                <%}%>
            </table>
        </td>
    </tr>
    <%} else {%>
    <tr>
        <td width="10%" valign="top">
            <b>Browser<%if (multipleBrowsersAllowed) {%>s<%}%>:</b>
        </td>
        <td>
            <table>
                <%
                    List<RemoteConfiguration> remoteConfigurations = ((JsUnitAggregateServer) server).getCachedRemoteConfigurations();
                    for (RemoteConfiguration remoteConfiguration : remoteConfigurations) {%>
                <tr>
                    <td>
                        <img src="<%=remoteConfiguration.getPlatformType().getLogoPath()%>" alt="<%=remoteConfiguration.getPlatformType().getDisplayName()%>" title="<%=remoteConfiguration.getPlatformType().getDisplayName()%>">
                    </td>
                    <td width="*">
                        <table width="*">
                            <%
                                List<Browser> browsers = remoteConfiguration.getBrowsers();
                                for (Browser browser : browsers) {
                            %>
                            <tr>
                                <td valign="top" nowrap align="left">
                                    <input type="checkbox" name="urlId_browserId" value="<%=browser.getId()%>" checked>
                                    <%if (browser.getType() != null) {%>
                                    <img src="<%=browser.getLogoPath()%>" alt="<%=browser.getDisplayName()%>" title="<%=browser.getDisplayName()%>">
                                    <%}%>
                                    <%=browser.getDisplayName()%>
                                </td>
                            </tr>
                            <%}%>
                        </table>
                    </td>
                </tr>
                <tr><td colspan="2"></td></tr>
                <%}%>
            </table>
        </td>
    </tr>
    <%}%>
    <tr>
        <td width="1">
            <b>Skin:</b>
        </td>
        <td>
            <select name="skinId">
                <%
                    for (Skin skin : server.getSkins()) {
                %><option value="<%=skin.getId()%>"><%=skin.getDisplayName()%></option>
                <%}%>
                <option value="">None (raw XML)</option>
            </select><br>
        </td>
    </tr>
</table>
