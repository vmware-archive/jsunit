<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.PlatformType" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.utility.StringUtility" %>
<%@ page import="net.jsunit.utility.SystemUtility" %>
<%
    JsUnitServer server = ServerRegistry.getServer();
    PlatformType platformType = PlatformType.resolve();
%>
<table height="90" width="100%" cellpadding="0" cellspacing="0" border="0" summary="jsUnit Information"
       bgcolor="#DDDDDD">
    <tr>
        <td width="1">
            <a href="http://www.jsunit.net" target="_blank"><img src="images/logo_jsunit.gif" alt="JsUnit" border="0"/></a>
        </td>
        <td width="50">&nbsp;</td>
        <td nowrap align="left">
            <table>
                <tr>
                    <td></td>
                    <td>
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
                    <td valign="middle">
                        <img src="<%=platformType.getLogoPath()%>" alt="<%=platformType.getDisplayName()%>">
                    </td>
                    <td>
                        Running on <%=SystemUtility.displayString()%>
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
                <img border="0" src="images/pivotal.gif" alt="Powered By Pivotal">
            </a>
        </td>

    </tr>
</table>
<br>
<%--
<%if (server.isAggregateServer()) {%>
    <b>Remote Machines:</b>
    <table>
        <%for (Configuration remoteConfiguration : ((JsUnitAggregateServer) server).getCachedRemoteConfigurations()) {%>
        <tr>
            <td>
                <%for (Browser browser : remoteConfiguration.getBrowsers()) {
                    if (browser.getType() != null) {%>
                <img src="<%=browser.getType().getLogoPath()%>" alt="<%=browser.getType().getDisplayName()%>">
                    <%}%>
                    <%=browser.getDisplayString()%>
                    <br>
                <%}%>
            </td>
        </tr>
        <%}%>
    </table>
<%}%>--%>
