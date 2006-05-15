<%@ page import="net.jsunit.JsUnitAggregateServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.configuration.RemoteConfiguration" %>
<%@ page import="net.jsunit.model.Browser" %>
<%@ page import="java.util.List" %>
<%JsUnitAggregateServer server = ServerRegistry.getAggregateServer();%>
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
<font size="-2">
    Because you are not signed in, you may select at most 3 browsers per test run. Want more?
    <a href="/jsunit/myaccountpage">Sign up</a> for a JsUnit account.
</font>