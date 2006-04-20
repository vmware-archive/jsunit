<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.utility.SystemUtility" %>
<%@ page import="net.jsunit.utility.StringUtility"%>
<%JsUnitServer server = ServerRegistry.getServer();%>
<table height="90" width="100%" cellpadding="0" cellspacing="0" border="0" summary="jsUnit Information"
       bgcolor="#DDDDDD">
    <tr>
        <td width="1">
            <a href="http://www.jsunit.net" target="_blank"><img src="images/logo_jsunit.gif" alt="JsUnit" border="0"/></a>
        </td>
        <td width="50">&nbsp;</td>
        <th nowrap align="left">
            <h4>
                <%if (!StringUtility.isEmpty(server.getConfiguration().getDescription())) {%>
                    <%=server.getConfiguration().getDescription()%> - 
                <%}%>
                JsUnit <%=SystemUtility.jsUnitVersion()%><%if (server.isAggregateServer()) {%> Aggregate<%}%> Server
            </h4>
            <font size="-2"><i>Running on <%=SystemUtility.displayString()%>
        </th>
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

