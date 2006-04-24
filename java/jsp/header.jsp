<%@ page import="net.jsunit.JsUnitAggregateServer" %>
<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.PlatformType" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.configuration.RemoteConfiguration" %>
<%@ page import="net.jsunit.model.Browser" %>
<%@ page import="net.jsunit.utility.StringUtility" %>
<%@ page import="net.jsunit.utility.SystemUtility" %>
<%
    JsUnitServer server = ServerRegistry.getServer();
    PlatformType platformType = PlatformType.resolve();
%>
<table height="90" width="100%" cellpadding="0" cellspacing="0" border="0" summary="jsUnit Information"
       bgcolor="#DDDDDD">
<tr>
<td width="1" valign="top">
    <a href="http://www.jsunit.net" target="_blank"><img src="images/logo_jsunit.gif" title="JsUnit" alt="JsUnit" border="0"/></a>
</td>
<td width="50">&nbsp;</td>
<td nowrap align="left">
    <table width="100%">
        <tr>
            <td width="1" rowspan="2" valign="top">
                <img src="<%=platformType.getLogoPath()%>" alt="<%=platformType.getDisplayName()%>" title="<%=platformType.getDisplayName()%>">
            </td>
            <td colspan="*">
                <b>
                    <%if (!StringUtility.isEmpty(server.getConfiguration().getDescription())) {%>
                    <%=server.getConfiguration().getDescription()%> -
                    <%}%>
                    JsUnit <%=SystemUtility.jsUnitVersion()%><%if (server.isAggregateServer()) {%>
                    Aggregate<%}%> Server
                </b>
            </td>
        </tr>
        <tr>
            <td>
                <table width="100%">
                    <tr>
                        <td colspan="100">
                            Running on <%=SystemUtility.displayString()%>
                        </td>
                    </tr>
                    <%if (!server.isAggregateServer()) {%>
                    <tr>
                        <td colspan="100">
                            <i>Available browsers:</i>
                            <%for (Browser browser : server.getConfiguration().getBrowsers()) {%>
                            <img src="<%=browser.getType().getLogoPath()%>" alt="<%=browser.getDisplayName()%>" title="<%=browser.getDisplayName()%>">
                            <%}%>
                        </td>
                    </tr>
                    <%} else {%>
                    <tr>
                        <td colspan="100">
                            <i>This server runs tests on these remote machines:</i>
                        </td>
                    </tr>
                    <tr>
                        <%for (RemoteConfiguration remoteConfiguration : ((JsUnitAggregateServer) server).getCachedRemoteConfigurations()) {%>
                        <td>
                            <table bgcolor="#EEEEEE">
                                <tr>
                                    <td align="center">
                                        <b>
                                            <%=remoteConfiguration.getDescription()%>
                                        </b>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center">
                                        <a href="<%=remoteConfiguration.getRemoteURL().toString()%>">
                                            <%=remoteConfiguration.getRemoteURL().getHost().toString()%>
                                        </a>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center">
                                        <table>
                                            <tr>
                                                <td>
                                                    <img src="<%=remoteConfiguration.getPlatformType().getLogoPath()%>" alt="<%=remoteConfiguration.getPlatformType().getDisplayName()%>" title="<%=remoteConfiguration.getPlatformType().getDisplayName()%>">
                                                </td>
                                                <td>
                                                    <%
                                                        for (Browser browser : remoteConfiguration.getBrowsers()) {
                                                            if (browser.getType() != null) {
                                                    %>
                                                    <img src="<%=browser.getType().getLogoPath()%>" alt="<%=browser.getDisplayName()%>" title="<%=browser.getDisplayName()%>">
                                                    <%}%>
                                                    <%}%>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="2">&nbsp;</td>
                        <%}%>
                    </tr>
                    <%}%>
                </table>
            </td>
        </tr>
    </table>
</td>
<td nowrap align="right" valign="middle">
    <font size="-2">
        <b><a href="http://www.jsunit.net/" target="_blank">www.jsunit.net</a></b><br>

        <div id="versionCheckDiv"><a href="javascript:checkForLatestVersion('latestversion')">Check for newer
            version</a></div>
    </font>
    <br>
    <a href="http://www.pivotalsf.com/" target="top">
        <img border="0" src="images/pivotal.gif" alt="Powered By Pivotal" title="Powered by Pivotal">
    </a>
</td>

</tr>
</table>
<br>