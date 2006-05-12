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
<table cellpadding="0" cellspacing="2">
    <tr>
        <%
            List<RemoteConfiguration> remoteConfigurations = ((JsUnitAggregateServer) server).getCachedRemoteConfigurations();
            for (int i = 0; i < remoteConfigurations.size(); i++) {
                RemoteConfiguration remoteConfiguration = remoteConfigurations.get(i);
        %>
        <td>
            <table style="background-color:#EEEEEE;" cellpadding="1" cellspacing="0">
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
                                    <table cellpadding="0" cellspacing="2">
                                        <tr>
                                            <td valign="middle">
                                                <input type="checkbox" id="urlId_browserId" name="urlId_browserId" value="<%=i%>_<%=browser.getId()%>">
                                            </td>
                                            <td valign="middle">
                                                <%if (browser._getType() != null) {%>
                                                <img src="<%=browser.getLogoPath()%>" alt="<%=browser.getDisplayName()%>" title="<%=browser.getDisplayName()%>">
                                                <%}%>
                                            </td>
                                            <td valign="middle">
                                                <font size="-2"><%=browser.getDisplayName()%></font>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <%}%>
                        </table>
                    </td>
                </tr>
            </table>
        </td><%}%>
    </tr>

</table>
<%} else {%>
<table>
    <%
        List<Browser> browsers = server.getConfiguration().getBrowsers();
        for (int i = 0; i < browsers.size(); i++) {
            Browser browser = browsers.get(i);
    %>
    <tr>
        <td valign="top" nowrap>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td valign="middle">
                        <input type="<%=multipleBrowsersAllowed ? "checkbox" : "radio"%>" id="browserId" name="browserId" value="<%=browser.getId()%>" <%if (!multipleBrowsersAllowed &&  i == 0) {%> checked<%}%>>
                    </td>
                    <td valign="middle">
                        <%if (browser._getType() != null) {%>
                        <img src="<%=browser.getLogoPath()%>" alt="<%=browser.getDisplayName()%>" title="<%=browser.getDisplayName()%>">
                        <%}%>
                    </td>
                    <td nowrap valign="middle">
                        <font size="-2"><%=browser.getDisplayName()%></font>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <%}%>
</table>
<%}%>
