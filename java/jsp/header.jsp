<%@ page import="net.jsunit.JsUnitAggregateServer" %>
<%@ page import="net.jsunit.PlatformType" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.configuration.RemoteConfiguration" %>
<%@ page import="net.jsunit.model.Browser" %>
<%@ page import="net.jsunit.utility.StringUtility" %>
<%
    JsUnitAggregateServer server = ServerRegistry.getAggregateServer();
    PlatformType platformType = server.getPlatformType();
%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td>&nbsp;</td>
<td>
    <div class="rb0roundbox">
        <div class="rb0top"><div></div></div>

        <div class="rb0content" align="center">
            <a href="http://www.jsunit.net"><img src="images/logo_jsunit.gif" title="JsUnit" alt="JsUnit" border="0"/></a>
        </div>

        <div class="rb0bot"><div></div></div>
    </div>
</td>
<td>&nbsp;</td>
<td nowrap align="center">
    <table cellpadding="0" cellspacing="2">
        <tr>
            <td>
                <div class="rb0roundbox" width="34">
                    <div class="rb0top"><div></div></div>

                    <div class="rb0content" align="center">
                        <table cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="center">
                                    <b>
                                        <%if (!StringUtility.isEmpty(server.getConfiguration().getDescription())) {%>
                                        <%=server.getConfiguration().getDescription()%> -
                                        <%}%>
                                        JsUnit Online Services
                                    </b>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    The JsUnit Online Services are tools that allow you to run your JsUnit tests on
                                    a variety<br>
                                    of browsers running on multiple operating systems. The services are available in a
                                    manual<br>
                                    form on these pages or in a programmatic form as web services. <a href="helppage">Learn
                                    more</a>.
                                </td>
                            </tr>
                        </table>
                    </div>

                    <div class="rb0bot"><div></div></div>
                </div>
            </td>
        </tr>
        <%if (false) {%>
        <tr>
            <td>
                <table width="100%">
                    <tr>
                        <%for (RemoteConfiguration remoteConfiguration : server.getCachedRemoteConfigurations()) {%>
                        <td>
                            <div class="rb0roundbox">
                                <div class="rb0top"><div></div></div>

                                <div class="rb0content" align="center">
                                    <table cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td width="1" rowspan="2">
                                                <img border="0" src="<%=remoteConfiguration.getPlatformType().getLogoPath()%>" alt="<%=remoteConfiguration.getPlatformType().getDisplayName()%>" title="<%=remoteConfiguration.getOsString()%>">
                                            </td>
                                            <td align="left">
                                                <%=remoteConfiguration.getOsString()%>
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
                                                                    if (browser._getType() != null) {
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
                </table>
            </td>
        </tr>
        <%}%>
    </table>
</td>
<td>&nbsp;</td>
<td nowrap align="center" valign="top">
    <table cellpadding="0" cellspacing="2" height="100%">
        <tr>
            <td valign="bottom">
                <div class="rb0roundbox">
                    <div class="rb0top"><div></div></div>

                    <div class="rb0content" align="center">
                        <table>
                            <tr>
                                <td>
                                    These are the <b>manual</b> services.
                                    <b><a href="createaccountpage">Sign up</a></b> for a JsUnit account<br>
                                    to get access to JsUnit web services using SOAP.
                                    <br>

                                    <b>Need help?</b> Email <a href="mailto:support@jsunit.net">support@jsunit.net</a>
                                    <br>
                                    <a href="http://www.jsunit.net/">jsunit.net</a>
                                    | <a href="http://blog.jsunit.net/">blog.jsunit.net</a>
                                    | <a href="http://group.jsunit.net/">group.jsunit.net</a><br>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <div class="rb0bot"><div></div></div>
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