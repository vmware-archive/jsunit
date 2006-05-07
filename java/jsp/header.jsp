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
<table width="100%" cellpadding="0" cellspacing="0" border="0" bgcolor="#e4ecec">
<tr>
<td>&nbsp;</td>
<td>
    <a href="http://www.jsunit.net"><img src="images/logo_jsunit.gif" title="JsUnit" alt="JsUnit" border="0"/></a>
</td>
<td>&nbsp;</td>
<td nowrap align="center">
<table cellpadding="0" cellspacing="2">
<tr>
    <td>
        <%if (!server.isAggregateServer()) {%>
        <div class="rb0roundbox">
            <div class="rb0top"><div></div></div>

            <div class="rb0content" style="width:200;" align="center">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td>
                            <img src="<%=platformType.getLogoPath()%>" alt="<%=platformType.getDisplayName()%>" title="<%=platformType.getDisplayName()%>">
                        </td>
                        <td>
                            <b>
                                <%if (!StringUtility.isEmpty(server.getConfiguration().getDescription())) {%>
                                <%=server.getConfiguration().getDescription()%> -
                                <%}%>
                                JsUnit <%=SystemUtility.jsUnitVersion()%> Server
                            </b>
                            <br>
                            Running on <%=server.getConfiguration().getSystemDisplayString()%>
                            <table>
                                <tr>
                                    <td>
                                        <i>Available browsers:</i>
                                    </td>
                                    <td>
                                        <%for (Browser browser : server.getConfiguration().getBrowsers()) {%>
                                        <img src="<%=browser.getLogoPath()%>" alt="<%=browser.getDisplayName()%>" title="<%=browser.getDisplayName()%>">
                                        <%}%>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="rb0bot"><div></div></div>
        </div>
        <%} else {%>
        <div class="rb2roundbox">
            <div class="rb2top"><div></div></div>

            <div class="rb2content" align="center">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td>
                            <b>
                                <%if (!StringUtility.isEmpty(server.getConfiguration().getDescription())) {%>
                                <%=server.getConfiguration().getDescription()%> -
                                <%}%>
                                JsUnit <%=SystemUtility.jsUnitVersion()%> Aggregate Server
                            </b>
                            (<%=((JsUnitAggregateServer) server).getCachedRemoteConfigurations().size()%> remote
                            servers)
                        </td>
                    </tr>
                </table>
            </div>

            <div class="rb2bot"><div></div></div>
        </div>
        <%}%>
    </td>
</tr>
<tr>
    <td>
        <table width="100%">
            <%if (!server.isAggregateServer()) {%>
            <%} else {%>
            <tr>
                <%for (RemoteConfiguration remoteConfiguration : ((JsUnitAggregateServer) server).getCachedRemoteConfigurations()) {%>
                <td>
                    <div class="rb0roundbox">
                        <div class="rb0top"><div></div></div>

                        <div class="rb0content" style="width:200;" align="center">
                            <table cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="1" rowspan="2">
                                        <a href="<%=remoteConfiguration.getRemoteURL().toString()%>"><img border="0" src="<%=remoteConfiguration.getPlatformType().getLogoPath()%>" alt="<%=remoteConfiguration.getPlatformType().getDisplayName()%>" title="<%=remoteConfiguration.getOsString()%>"></a>
                                    </td>
                                    <td align="left">
                                        <%if (!StringUtility.isEmpty(remoteConfiguration.getDescription())) {%>
                                        <%=remoteConfiguration.getDescription()%> -
                                        <%}%>
                                        <a href="<%=remoteConfiguration.getRemoteURL().toString()%>"><%=remoteConfiguration.getRemoteURL().getHost()%></a>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center">
                                        <table cellpadding="1" cellspacing="0">
                                            <tr>
                                                <td>
                                                    Browsers:
                                                </td>
                                                <td>
                                                    <%
                                                        for (Browser browser : remoteConfiguration.getBrowsers()) {
                                                            if (browser.getType() != null) {
                                                    %>
                                                    <img src="<%=browser.getLogoPath()%>" alt="<%=browser.getDisplayName()%>" title="<%=browser.getDisplayName()%>">
                                                    <%}%>
                                                    <%}%>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>

                        </div>

                        <div class="rb0bot"><div></div></div>
                    </div>
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
<td>&nbsp;</td>
<td nowrap align="center" valign="top">
    <table cellpadding="0" cellspacing="2" height="100%">
        <tr>
            <td nowrap align="center" valign="top">
                [ <a href="http://www.jsunit.net/">jsunit.net</a>
                | <a href="http://blog.jsunit.net/">blog.jsunit.net</a>
                | <a href="http://group.jsunit.net/">group.jsunit.net</a> ]
            </td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td valign="bottom">
                <div class="rb2roundbox">
                    <div class="rb2top"><div></div></div>

                    <div class="rb2content" align="center">
                        <b><a href="#">Sign up</a></b> for a JsUnit account<br>
                        to get programmatic access to the<br>
                        fragment and upload runner services.
                    </div>

                    <div class="rb2bot"><div></div></div>
                </div>
            </td>
        </tr>
    </table>
</td>
<td>&nbsp;</td>
<%if (false) {%>
<td nowrap align="right" valign="middle">
    <div id="versionCheckDiv"><a href="javascript:checkForLatestVersion('latestversion')">Check for newer
        version</a></div>
    <br>
    <a href="http://www.pivotalsf.com/" target="top">
        <img border="0" src="images/pivotal.gif" alt="Powered By Pivotal" title="Powered by Pivotal">
    </a>
</td>
<td>&nbsp;</td>
<%}%>
</tr>
</table>