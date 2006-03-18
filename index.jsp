<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.configuration.Configuration" %>
<%@ page import="net.jsunit.configuration.ConfigurationProperty" %>
<%@ page import="net.jsunit.utility.SystemUtility" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<%Configuration configuration = server.getConfiguration();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JsUnit <%if (server.isFarmServer()) {%> Farm<%}%> Server</title>
    <script type="text/javascript" src="app/jsUnitCore.js"></script>
    <script language="JavaScript" type="text/javascript" src="app/jsUnitVersionCheck.js"></script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body>
<table width="100%" cellpadding="0" cellspacing="0" border="0" summary="jsUnit Information" bgcolor="#DDDDDD">
    <tr>
        <td width="1">
            <a href="http://www.jsunit.net"><img src="images/logo_jsunit.gif" alt="JsUnit" border="0"/></a>
        </td>
        <td width="50">&nbsp;</td>
        <th nowrap align="left">
            <h4>JsUnit <%=SystemUtility.jsUnitVersion()%><%if (server.isFarmServer()) {%> Farm<%}%> Server</h4>
            <font size="-2"><i>Running on <%=SystemUtility.displayString()%><br/>
        </th>
        <td nowrap align="right" valign="middle">
            <font size="-2">
                <a href="http://www.jsunit.net/" target="_blank">www.jsunit.net</a><br>
                <div id="versionCheckDiv"><a href="javascript:checkForLatestVersion('latestversion')">Check for newer version</a></div>
            </font>
            <a href="http://www.pivotalsf.com/" target="top"><img border="0" src="images/powerby-transparent.gif" alt="Powered By Pivotal"></a>
        </td>

    </tr>
</table>
<h4>
    Server configuration
</h4>
<table border="0">
    <tr>
        <th nowrap align="right">Server type:</th>
        <td width="10">&nbsp;</td>
        <td><%=server.serverType().getDisplayName()%></td>
    </tr>
    <%
        for (ConfigurationProperty property : configuration.getRequiredAndOptionalConfigurationProperties(server.serverType())) {
    %>
    <tr>
        <th nowrap align="right"><%=property.getDisplayName()%>:</th>
        <td width="10">&nbsp;</td>
        <td>
            <%
                for (String valueString : property.getValueStrings(configuration)) {
            %><div><%
            if (valueString != null) {
                if (property.isURL()) {
        %><a href="<%=valueString%>"><%=valueString%></a><%
        } else {
        %><%=valueString%><%
                }
            }
        %></div><%
            }
        %>
        </td></tr>
    <%
        }
    %>
</table>
<h4>testRunner.html</h4>
The manual Test Runner is at <a href="./testRunner.html">testRunner.html</a>.
<h4>config</h4>
You can see the configuration of this server by going to <a href="/jsunit/config">config</a>. The configuration is
displayed as XML. config is usually used programmatically.
<h4>runner</h4>
You can see tell the server to run JsUnit tests using the runner command.
You can run using the server's default URL for tests by going to <a href="/jsunit/runner">runner</a>,
or you can specify a custom URL using the following form.
<form action="/jsunit/runner" method="get">
    URL: <input type="text" name="url" size="100"/>
    <input type="submit" value="go"/>
</form>
<%if (!server.isFarmServer()) {%>
<h4>displayer</h4>
You can view the logs of past runs using the displayer command.
Use this form to specify the ID of the run you want to see:
<form action="/jsunit/displayer">
    ID: <input type="text" name="id" size="20"/>
    <input type="submit" value="go"/>
</form>
<%}%>
</body>
</html>