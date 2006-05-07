<%@ page import="net.jsunit.JsUnitAggregateServer" %>
<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.configuration.RemoteConfiguration" %>
<%@ page import="net.jsunit.model.Browser" %>
<%@ page import="java.util.List" %>
<%
    JsUnitServer server = ServerRegistry.getServer();
    boolean multipleBrowsersAllowed = Boolean.parseBoolean(request.getParameter("multipleBrowsersAllowed"));
%>
<%if (server.isAggregateServer()) {%>
<table><tr>
    <%
        List<RemoteConfiguration> remoteConfigurations = ((JsUnitAggregateServer) server).getCachedRemoteConfigurations();
        for (int i = 0; i < remoteConfigurations.size(); i++) {
            RemoteConfiguration remoteConfiguration = remoteConfigurations.get(i);
    %>
    <td>
        <table style="border-color:#000000;border:1px;border-style:solid">
            <tr>
                <td>
                    <img src="<%=remoteConfiguration.getPlatformType().getLogoPath()%>" alt="<%=remoteConfiguration.getPlatformType().getDisplayName()%>" title="<%=remoteConfiguration.getOsString()%>">
                </td>
                <td valign="middle">
                    <table cellpadding="0" cellspacing="0">
                        <%
                            List<Browser> browsers = remoteConfiguration.getBrowsers();
                            for (Browser browser : browsers) {
                        %>
                        <tr>
                            <td valign="top" nowrap align="left">
                                <input type="checkbox" name="urlId_browserId" value="<%=i%>_<%=browser.getId()%>" checked>
                                <%if (browser.getType() != null) {%>
                                <img src="<%=browser.getLogoPath()%>" alt="<%=browser.getDisplayName()%>" title="<%=browser.getDisplayName()%>">
                                <%}%>
                                <font size="-2"><%=browser.getDisplayName()%></font>
                            </td>
                        </tr>
                        <%}%>
                    </table>
                </td>
            </tr>
        </table>
    </td><%}%>
</tr></table>
<%} else {%>
<table>
    <%
        List<Browser> browsers = server.getConfiguration().getBrowsers();
        for (int i = 0; i < browsers.size(); i++) {
            Browser browser = browsers.get(i);
    %>
    <tr>
        <td valign="top" nowrap>
            <input type="<%=multipleBrowsersAllowed ? "checkbox" : "radio"%>" name="browserId" value="<%=browser.getId()%>" <%if (multipleBrowsersAllowed || i == 0) {%> checked<%}%>>
            <%if (browser.getType() != null) {%>
            <img src="<%=browser.getLogoPath()%>" alt="<%=browser.getDisplayName()%>" title="<%=browser.getDisplayName()%>">
            <%}%>
            <%=browser.getDisplayName()%>
        </td>
    </tr>
    <%}%>
</table>
<%}%>
