<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.configuration.Configuration" %>
<%@ page import="net.jsunit.configuration.ConfigurationProperty" %>
<%@ page import="net.jsunit.model.Browser" %>
<%@ page import="net.jsunit.utility.SystemUtility" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<%Configuration configuration = server.getConfiguration();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JsUnit <%if (server.isFarmServer()) {%> Farm<%}%> Server</title>
    <script type="text/javascript" src="app/jsUnitCore.js"></script>
    <script type="text/javascript" src="app/jsUnitVersionCheck.js"></script>
    <script type="text/javascript">
        function selectCell(selectedCellName) {
            updateDiv("testRunnerCell", selectedCellName);
            updateDiv("configCell", selectedCellName);
            updateDiv("runnerCell", selectedCellName);
            updateDiv("displayerCell", selectedCellName);
        }

        function updateDiv(divName, selectedDivName) {
            var isSelected = divName == selectedDivName;
            var theDiv = document.getElementById(divName);
            theDiv.style.visibility = isSelected ? "visible" : "hidden";

        }
    </script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body onload="selectCell('runnerCell')">
<table width="100%" cellpadding="0" cellspacing="0" border="0" summary="jsUnit Information" bgcolor="#DDDDDD">
    <tr>
        <td width="1">
            <a href="http://www.jsunit.net" target="_blank"><img src="images/logo_jsunit.gif" alt="JsUnit" border="0"/></a>
        </td>
        <td width="50">&nbsp;</td>
        <th nowrap align="left">
            <h4>JsUnit <%=SystemUtility.jsUnitVersion()%><%if (server.isFarmServer()) {%> Farm<%}%> Server</h4>
            <font size="-2"><i>Running on <%=SystemUtility.displayString()%><br/>
        </th>
        <td nowrap align="right" valign="middle">
            <font size="-2">
                <b><a href="http://www.jsunit.net/" target="_blank">www.jsunit.net</a></b><br>

                <div id="versionCheckDiv"><a href="javascript:checkForLatestVersion('latestversion')">Check for newer
                    version</a></div>
            </font>
            <a href="http://www.pivotalsf.com/" target="top"><img border="0" src="images/powerby-transparent.gif"
                                                                  alt="Powered By Pivotal"></a>
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
<br>
<h4>
    Services
</h4>
<table cellpadding="0" cellspacing="0">
    <tr>
        <td width="1" align="center" id="runnerCellHeader">
            <a href="javascript:selectCell('runnerCell')">runner</a>
        </td>
        <td>&nbsp;</td>
        <td width="1" align="center" id="displayerCellHeader">
            <a href="javascript:selectCell('displayerCell')">displayer</a>
        </td>
        <td>&nbsp;</td>
        <td width="1" align="center" id="testRunnerCellHeader">
            <a href="javascript:selectCell('testRunnerCell')">testRunner.html</a>
        </td>
        <td>&nbsp;</td>
        <td width="1" align="center" id="configCellHeader">
            <a href="javascript:selectCell('configCell')">config</a>
        </td>
        <td width="90%">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="8" width="100%">&nbsp;</td>
    </tr>
    <tr width="100%" id="runnerCell" style="visibility:hidden;position:absolute">
        <td colspan="8" width="100%">
            You can see tell the server to run JsUnit tests using the <i>runner</i> servlet.
            You can run using the server's default URL for tests by going to <a href="/jsunit/runner">runner</a>,
            or you can specify a custom URL and/or browser ID using the following form.<br><br>

            <form action="/jsunit/runner" method="get" name="runnerForm">
                URL: <input type="text" name="url" size="100"
                            value=""/>
                <br>
                <font size="-2"><i>e.g.
                    http://www.jsunit.net/runner/testRunner.html?testPage=http://www.jsunit.net/runner/tests/jsUnitTestSuite.html</i>
                </font><br>
                <%if (!server.isFarmServer()) {%>
                Browser:
                <select name="browserId">
                    <option value="">(All browsers)</option>
                    <%
                        for (Browser browser : configuration.getBrowsers()) {
                    %><option value="<%=browser.getId()%>"><%=browser.getFileName()%></option>
                    <%}%>
                </select><br>
                <%}%>
                <input type="submit" value="go"/>
            </form>
            <br>
        </td>
    </tr>
    <%if (!server.isFarmServer()) {%>
    <tr id="displayerCell" style="visibility:hidden;position:absolute">
        <td colspan="8" width="100%">

            You can view the logs of past runs using the displayer command.
            Use this form to specify the ID of the run you want to see:<br><br>

            <form action="/jsunit/displayer" name="displayerForm">
                ID: <input type="text" name="id" size="20"/><br>
                Browser:
                <select name="browserId">
                    <%
                        for (Browser browser : configuration.getBrowsers()) {
                    %><option value="<%=browser.getId()%>"><%=browser.getFileName()%></option>
                    <%}%>
                </select><br>
                <input type="submit" value="go"/>
            </form>
            <br>
            <%}%>
        </td>
    </tr>
    <tr id="testRunnerCell" style="visibility:hidden;position:absolute">
        <td colspan="8" width="100%">
            The manual Test Runner is at <a id="testRunnerHtml" href="./testRunner.html">testRunner.html</a>.
            <br>&nbsp;
        </td>
    </tr>
    <tr id="configCell" style="visibility:hidden;position:absolute">
        <td colspan="8" width="100%">
            You can see the configuration of this server by going to <a id="config" href="/jsunit/config">config</a>.
            The
            configuration is displayed as XML. config is usually only used programmatically.
            <br>&nbsp;
        </td>
    </tr>
</table>
</body>
</html>