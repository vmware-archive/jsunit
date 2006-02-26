<%@ page import="net.jsunit.JsUnitServer"%>
<%@ page import="net.jsunit.ServerRegistry"%>
<%@ page import="net.jsunit.utility.SystemUtility"%>
<%@ page import="net.jsunit.utility.XmlUtility"%>
<%JsUnitServer server = ServerRegistry.getServer();%>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
    <title>JsUnit Server</title>
    <script type="text/javascript" src="app/jsUnitCore.js"></script>
</head>
<body>
<table width="100%" bgcolor="#EEEEEE">
    <tr>
        <td width="1">
            <a href="http://www.jsunit.net"><img src="images/logo_jsunit.gif" alt="JsUnit" border="0"/></a>
        </td>
        <td width="50">&nbsp;</td>
        <td valign="middle" align="left">
            <h4>JsUnit <%=SystemUtility.jsUnitVersion()%><%if (server.serverType().isFarm()){%> Farm<%}%> Server</h4>
            <i>Running on <%=SystemUtility.osString()%>.</i>
        </td>
    </tr>
</table>
<h4>
    Server configuration:
</h4>
<p>
    <%=XmlUtility.asPrettyString(server.asXml())%>
</p>
<p>
    The following commands are available on this JsUnit server. For more information, see <a href="http://www.jsunit.net">jsunit.net</a>.<br/>
</p>
<h4>testRunner.html</h4>
The manual Test Runner is at <a href="testRunner.html">testRunner.html</a>.
<h4>config</h4>
You can see the configuration of this server by going to <a href="config">config</a>.
<h4>runner</h4>
You can see tell the server to run JsUnit tests using the runner command.
You can run using the server's default URL for tests by going to <a href="runner">runner</a>,
or you can specify a custom URL using this form:
<form action="runner" method="get">
    URL: <input type="text" name="url" size="100"/>
    <input type="submit" value="go"/>
</form>
<h4>displayer</h4>
You can view the logs of past runs using the displayer command.
Use this form to specify the ID of the run you want to see:
<form action="displayer">
    ID: <input type="text" name="id" size="20"/>
    <input type="submit" value="go"/>
</form>
</body>
</html>